# Voltereta Croqueta
___
## Index
- [**Phase 0**](#phase-0)
    - [**:busts_in_silhouette: Development team member**](#busts_in_silhouette-development-team-members)
    - [**:hammer_and_wrench: Team coordination tool**](#hammer_and_wrench-team-coordination-tool)
    - [**:memo: Entities**](#memo-entities)
    - [**:gear: Functionalities**](#gear-functionalities)
    - [**:key: User permissions**](#key-user-permissions)
    - [**:framed_picture: Images**](#framed_picture-Images)
    - [**:bar_chart: Graphics**](#bar_chart-Graphics)
    - [**:bulb: Complementary technology**](#bulb-complementary-technology)
    - [**:chart_with_upwards_trend: Algorithm and advanced query**](#chart_with_upwards_trend-algorithm-and-advanced-query)

# Phase 0
___
## :busts_in_silhouette: Development team members

| Name and surname         |mail|github|
|--------------------------|----|------|
| Nicolás Hernández Tejero | n.hernandezt.2021@alumnos.urjc.es | nicohht |
| Hugo Sánchez Torres      | h.sanchez.2021@alumnos.urjc.es | stHugo010 |
| Juan Ramírez Blanco      | j.ramirez.2021@alumnos.urjc.es | juanramirezbl |
| Jesús López Esquinas     | j.lopeze.2020@alumnos.urjc.es | jesuslpzZz |
| Víctor Muñoz Regalón     | v.munozr.2020@alumnos.urjc.es | victoor-78 |

___

## :hammer_and_wrench: Team coordination tool
### Trello
This tool offers a Kanban-like organization, allowing us to view at any time and from any location the section of the software that is under development, completed, or pending.
<a href="https://trello.com/b/k80Jt9Pc/daw-wabapp07">Trello link</a>

___
## Main aspects of the web application
___
### :memo: Entities
The main entities that the application will manage are:
- Users
- Orders
- Products
- Promotions
- Reviews

:twisted_rightwards_arrows: <b>Main relations between entities: </b>
Users can be anonymous users, admins, or clients.
Customers can place orders with one or more dishes and rate each dish.
Admins can add dishes to the menu.
Orders contain the dishes, the price, the user information and delivery adress.

___
### :gear: Functionalities
- Create a new tournament  with all its attributes (name, date, stadium, teams participating).
- Add teams  to a tournament.
- Add players to a team.
- Generate matches for each round of the tournament based on the number of teams in it.
- Show match results after they have been played.
- Access to information about users, teams, tournaments, matches, and stadiums.
- User registration and login system.
- Write a match report.
- Show statistics and graphics about the performance of players or teams.
- Follow your favorite teams and players.
- Use search bar.
- Download match reports.
- Consult weather forecast.
- Send notifications about a match.
- Modify personal profile.
___

### :key: User permissions
The permissions of each of the user types are:
- <b>Anonymous</b>: View the menu and dishes, search for dishes, view promotions, reserve a table at the restaurant, check out news and updates, about us section, view the restaurant's location.
- <b>Registered</b>: Order for delivery, rate orders, access order history, view public ratings for each dish and your own ratings for each dish.
- <b>Admin</b>: All the permissions of a registered user, remove dishes from the menu, modify dishes, modify reservations, add new dishes to menu, block users, unblock users, cancel orders.
___
### :framed_picture: Images
The images mentioned are related to the entities mentioned above.
- Favicon of the web page.
- Team Icons.
- Player Photos.
- Stands Image (Stadium).
- Tournament logo.
___
### :bar_chart: Graphics
The information that will be displayed using graphics is:
- Top Scorers: Table
- Goals per team: Column chart
- Number of goals scored vs goals conceded: Bar graph
- Average age of players: Pie chart

___
### :bulb: Complementary technology
The complementary technology to be used is:
- Our application allows users to generate a PDF receipt for their orders.
- Our application calculates the estimated delivery time based on the restaurant's address and the delivery address provided by the customer.


___

### :chart_with_upwards_trend: Algorithm and advanced query
The algorithm or advanced query to be implemented is:
- Automatic classification when submitting the match report.
- Advanced statistics of  matches by team/player.
- Search bar on teams and players pages.
___
