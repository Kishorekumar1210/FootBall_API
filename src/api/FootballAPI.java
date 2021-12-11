package api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class FootballAPI {

	public static void main(String[] args) {

		RestAssured.baseURI = "https://api.football-data.org/v2/competitions";

		String response = given().header("X-Auth-Token", "896d94af19a2407f8c3eab10f48b34f7").when().get().then()
				.assertThat().statusCode(200).extract().response().asString();
		// System.out.println(response);
		JsonPath js = new JsonPath(response);
		int countofCompetition = js.getInt("competitions.size()");
		// System.out.println(countofCompetition);
		for (int i = 0; i < countofCompetition; i++) {
			int id = js.getInt("competitions[" + i + "].id");
			if (id == 2015) {
				System.out.println("ID from the GET request : " + id);
				
				RestAssured.baseURI = "https://api.football-data.org/v2/competitions/" + id + "/matches";
				
				String res = given().header("X-Auth-Token", "896d94af19a2407f8c3eab10f48b34f7").when().get().then()
						.assertThat().statusCode(200).extract().response().asString();
				
				// System.out.println(res);
				JsonPath js1 = new JsonPath(res);
				int count = js1.getInt("matches.size()");
				// System.out.println(count);
				int counter = 0;
				for (int i1 = 0; i1 < count; i1++) {
					String awayTeam = js1.get("matches[" + i1 + "].score.winner");
					if (!(awayTeam == null) && awayTeam.equals("AWAY_TEAM")) {
						String awayTeamNames = js1.get("matches[" + i1 + "].awayTeam.name");
						System.out.println(awayTeamNames);
						if (awayTeamNames.equals("Paris Saint-Germain FC")) {
							counter++;
							
							RestAssured.baseURI = "https://api.football-data.org/v2/players/" + id + "";
							
							String response1 = given().header("X-Auth-Token", "896d94af19a2407f8c3eab10f48b34f7").when()
									.get().then().assertThat().statusCode(200).extract().response().asString();
							
							JsonPath js2 = new JsonPath(response1);
							String playerName = js2.get("firstName");
							if (counter == 1)
								System.out.println("Youngest FootBall Player : " + playerName);
						}
					} else {
						// System.out.println("Other than Away Team");
					}
				}
				int sum = 0;
				for (int i2 = 0; i2 < count; i2++) {
					String drawMatch = js1.get("matches[" + i2 + "].score.winner");
					if (!(drawMatch == null) && drawMatch.equals("DRAW")) {
						int drawTeamGoals = js1.getInt("matches[" + i2 + "].score.fullTime.homeTeam");
						sum = sum + drawTeamGoals;
						// System.out.println("Total goals : " + sum);
					} else {
						// System.out.println("Not goals Team");
					}
				}
				System.out.println("Total goals : " + sum);
			} else {
				// System.out.println("ID is not matched : " + id);
			}
		}

	}
}
