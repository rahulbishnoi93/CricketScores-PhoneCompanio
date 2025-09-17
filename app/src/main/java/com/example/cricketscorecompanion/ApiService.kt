package com.example.cricketscorecompanion

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("live")
    suspend fun getLiveMatches(): List<LiveMatch>

    @GET("recent")
    suspend fun getRecentMatches(): List<RecentMatch>

    @GET("schedule")
    suspend fun getSchedule(): List<ScheduleMatch>

    @GET("match/{id}")
    suspend fun getMatchDetails(@Path("id") id: String): MatchDetails

    @GET("allMatches")
    suspend fun getAllMatches(): AllMatches
}

@Serializable
data class LiveMatch(
    val liveMatchSummary: String,
    val matchId: String,
    val team1: Team,
    val team2: Team
)
@Serializable
data class AllMatches(
    val live_matches: List<LiveMatch>,
    val recent_matches: List<RecentMatch>,
    val upcoming_matches: List<ScheduleMatch>
)

/**
 * This data class defines a team with its score and name.
 */
@Serializable
data class Team(
    val score: String,
    val team: String
)

@Serializable
data class MatchDetails(
    val batsmen: List<Batter> = emptyList(),
    val bowlers: List<Bowler> = emptyList(),
    val player_of_the_match: String? = null,
    val status: String? = null,
    val Livestatus: String? = null,
    val match_title: String? = null,
)

@Serializable
data class Batter(
    val balls: String? = null,
    val fours: String? = null,
    val name: String,
    val runs: String? = null,
    val sixes: String? = null,
    val strike_rate: String? = null,
)

@Serializable
data class Bowler(
    val econ: String? = null,
    val maidens: String? = null,
    val name: String,
    val overs: String? = null,
    val runs: String? = null,
    val wickets: String? = null,
)
@Serializable
data class RecentMatch(
    val date: String,
    val matchId: String,
    val matchSummary: String,
    val result: String,
    val team1: Team,
    val team2: Team
)
@Serializable
data class ScheduleMatch(
    val date: String,
    val details: String,
    val team1: String,
    val team2: String,
    val time: String,
)
