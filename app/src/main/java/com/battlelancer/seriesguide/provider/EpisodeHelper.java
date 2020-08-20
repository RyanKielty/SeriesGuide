package com.battlelancer.seriesguide.provider;

import static com.battlelancer.seriesguide.provider.SeriesGuideDatabase.Tables.EPISODES;

import androidx.annotation.Nullable;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.battlelancer.seriesguide.model.EpisodeWithShow;
import com.battlelancer.seriesguide.model.SgEpisode;
import com.battlelancer.seriesguide.model.SgEpisodeSeasonAndShow;
import com.battlelancer.seriesguide.model.SgShow;
import com.battlelancer.seriesguide.ui.episodes.EpisodeFlags;

/**
 * Data Access Object for the episodes table.
 */
@Dao
public interface EpisodeHelper {

    /**
     * For testing: Get the first episode from the table.
     */
    @Query("SELECT * FROM " + EPISODES + " LIMIT 1")
    SgEpisode getEpisode();

    /**
     * For testing: Get single episode.
     */
    @Query("SELECT * FROM " + EPISODES + " WHERE _id=:episodeTvdbId")
    SgEpisode getEpisode(int episodeTvdbId);

    @Nullable
    @Query("SELECT season_id, season, series_id  FROM episodes WHERE _id=:episodeTvdbId")
    SgEpisodeSeasonAndShow getEpisodeMinimal(int episodeTvdbId);

    @RawQuery(observedEntities = {SgEpisode.class, SgShow.class})
    DataSource.Factory<Integer, EpisodeWithShow> getEpisodesWithShow(SupportSQLiteQuery query);

    @Query("UPDATE episodes SET watched = " + EpisodeFlags.WATCHED
            + ", plays = plays + 1 WHERE _id=:episodeTvdbId")
    int setWatchedAndAddPlay(int episodeTvdbId);

    @Query("UPDATE episodes SET watched = " + EpisodeFlags.UNWATCHED
            + ", plays = 0 WHERE _id=:episodeTvdbId")
    int setNotWatchedAndRemovePlays(int episodeTvdbId);

    @Query("UPDATE episodes SET watched = " + EpisodeFlags.SKIPPED + " WHERE _id=:episodeTvdbId")
    int setSkipped(int episodeTvdbId);
}
