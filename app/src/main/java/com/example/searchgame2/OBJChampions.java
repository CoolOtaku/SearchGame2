package com.example.searchgame2;

public class OBJChampions {

    String team_logo_1;
    String team_logo_2;
    String name_team_1;
    String name_team_2;
    String game_name;
    String stream_link;

    public OBJChampions(String team_logo_1, String team_logo_2, String name_team_1, String name_team_2, String game_name, String stream_link) {
        this.team_logo_1 = team_logo_1;
        this.team_logo_2 = team_logo_2;
        this.name_team_1 = name_team_1;
        this.name_team_2 = name_team_2;
        this.game_name = game_name;
        this.stream_link = stream_link;
    }

    public String getTeam_logo_1() {
        return team_logo_1;
    }

    public String getTeam_logo_2() {
        return team_logo_2;
    }

    public String getName_team_1() {
        return name_team_1;
    }

    public String getName_team_2() {
        return name_team_2;
    }

    public String getGame_name() {
        return game_name;
    }

    public String getStream_link() {
        return stream_link;
    }

}
