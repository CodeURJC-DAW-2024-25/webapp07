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
- [**Phase 1**](#phase-1)
  - [**:computer: Screens**](#computer-Screens) 
      - [**Home-page**](#Home-page)
      - [**Register/Login**](#Register-Login)
      - [**About-us**](#About-us)
      - [**Admin-actions-confirmed**](#Admin-actions-confirmed)
      - [**Booking**](#Booking)
      - [**Dish-information**](#Dish-information)
      - [**Edit-dish**](#Edit-dish)
      - [**Edit-profile**](#Edit-profile)
      - [**Error**](#Error)
      - [**Faqs**](#Faqs)
      - [**Menu**](#Menu)
      - [**New-dish**](#New-dish)
      - [**Order-confirmation-screen**](#Order-confirmation-screen)
      - [**Order-manager**](#Order-manager)
      - [**Order-summary**](#Order-summary)
      - [**Order-summary-admin**](#Order-summary-admin)
      - [**Orders-list**](#Orders-list)
      - [**Pickup-delivery-order**](#Pickup-delivery-order)
      - [**Profile**](#Profile)
      - [**Rate-order**](#Rate-order)
      - [**Ban-message**](#Ban-message)
      - [**Manage-user**](#Manage-user)
      - [**Restaurant-availability**](#Restaurant-availability)
      - [**Restaurant-information**](#Restaurant-information)
      - [**Restaurants**](#Restaurants)
  - [**:diamonds:Flow Diagram**](#diamonds-Flow-Diagram)
  - [**Phase 2**](#phase-2)
    - [**:computer: New Screens**](#computer-New-Screens)
    - [**:diamonds: New Flow Diagram**](#diamonds-New-Flow-Diagram)
    - [**:rocket: Execution Instructions**](#rocket-Execution-Instructions)
    - [**:file_folder:Diagram with the database entities**](#file_folder-Diagram-with-the-database-entities)
    - [**:art:Class diagram and templates**](#art-Class-diagram-and-templates)
    - [**:raised_hand: Participation**](#raised_hand-Participation)

# Phase 0
___
## :busts_in_silhouette: Development team members

| Name and surname         | mail                              | github        |
|--------------------------|-----------------------------------|---------------|
| Nicolás Hernández Tejero | n.hernandezt.2021@alumnos.urjc.es | nicohht       |
| Hugo Sánchez Torres      | h.sanchez.2021@alumnos.urjc.es    | stHugo010     |
| Juan Ramírez Blanco      | j.ramirez.2021@alumnos.urjc.es    | juanramirezbl |
| Jesús López Esquinas     | j.lopeze.2020@alumnos.urjc.es     | jesuslpzZz    |
| Víctor Muñoz Regalón     | v.munozr.2020@alumnos.urjc.es     | victoor-78    |

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
- Dishes
- Promotions
- Reviews
- Restaurants
- Reservations

:twisted_rightwards_arrows: <b>Main relations between entities: </b>
Users can be anonymous users, admins, or customers.
Customers can place orders with one or more dishes and rate each dish apart from make reservations at one of the restaurants.
Admins can add dishes to the menu.
Orders contain the dishes, the price, the user information and delivery address.
Restaurants contain dishes, reservations, reviews and promotions. 
Dishes contain the promotions about them.

___
### :gear: Functionalities
- Register/Login system for accounts
- Modify account information
- View the restaurant information
- View the menu dishes
- Search with filters
- Book a restaurant table
- Place an order for take-away or delivery
- Check order history
- Rate the dishes
- Download the digital receipt as a PDF file
- Send notifications about orders
- Modify and manage the orders, reservations, menu and products
- Manage user accounts
- Consult graphs about orders
___

### :key: User permissions
The permissions of each of the user types are:
- <b>Anonymous</b>: View the menu and dishes, search for dishes, view promotions, reserve a table at the restaurant, check out news and updates, about us section, view the restaurant's location.
- <b>Registered</b>: Order for delivery, rate orders, access order history, view public ratings for each dish and your own ratings for each dish.
- <b>Admin</b>: All the permissions of a registered user, remove dishes from the menu, modify dishes, modify reservations, add new dishes to menu, block users, unblock users, cancel orders, view the graphs with order statistics.
___
### :framed_picture: Images
The images mentioned are related to the entities mentioned above.
- Favicon of the web page.
- Dish Photos.
- Stands Image (Restaurant).
- Restaurant logo.
___
### :bar_chart: Graphics
The information that will be displayed using graphics is:
- Average orders per hour: Table
- Top 10 best-selling dishes with the number of orders on that day: Table
- The 15 best-rated dishes along with their number of ratings: Table

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

___
# Phase 1
___
#  :computer: Screens
___

## Home-page:
Voltereta Croqueta's home screen features a welcoming interface with navigation options. It highlights the restaurant's main features, such as expert chefs, quality food, online ordering and 24/7 service. It also includes a welcome section describing the restaurant's experience and history, along with information about years of experience and popular chefs.

![*Home-page*](readme-img/phase_01/index.png)

___
## Register-Login:
The login screen for Voltereta Croqueta provides a simple and user-friendly interface for users to access their accounts. It features fields for entering a username and password, along with a "LOGIN" button to submit the credentials. Additionally, there are options to sign up for new users who do not yet have an account.
![*Register-Login*](readme-img/phase_01/login.png)

___

## About-us:
This screen provides detailed information about Voltereta Croqueta, highlighting its culinary philosophy and commitment to fresh ingredients and authentic recipes. It includes a brief history of the restaurant and a list of the master chefs, each specializing in different types of cuisine.
![*About-us*](readme-img/phase_01/about.png)

___

## Admin-actions-confirmed:
This screen confirms that an operation (such as a reservation or order) has been successfully completed. It displays a clear confirmation message and a button to return to the home page.
![*Admin-actions-confirmed*](readme-img/phase_01/admin-actions-confirmed.png)

___

## Booking:
This screen allows users to book a table at Voltereta Croqueta. It includes options to choose a restaurant, specify the number of people, select the date and time, and provide reservation details such as name, last name, email, and phone number. A "CONFIRM ORDER" button finalizes the reservation.
![*Booking*](readme-img/phase_01/booking.png)

___

## Booking-confirmation:
This screen confirms that a user's reservation has been successfully placed. It provides a thank-you message and informs the user that they will receive a confirmation email shortly. A "BACK TO HOME" button allows users to return to the main page.
![*Order-confirmation-screen*](readme-img/phase_01/booking-confirmation.png)

___

## Dish-information:
This screen provides detailed information about a specific dish. It includes the dish's description, price, ingredients, and allergen information. Additional notes remind users to inform the restaurant of any dietary restrictions. Options to edit the dish, mark it as unavailable, or delete it are available for management purposes.
![*Dish-information*](readme-img/phase_01/dish-information.png)

___

## Edit-dish:
This screen allows for the editing of dish details, such as the name, description, price, and ingredients. It also includes a comprehensive list of allergens and dietary preferences, such as vegan options. A "SAVE CHANGES" button is provided to update the dish information.
![*Edit-dish*](readme-img/phase_01/edit-dish.png)

___

## Edit-profile:
This screen allows users to view and edit their profile information. It includes fields for name, email, phone number, and address. Users can save changes or cancel the edits. The interface is straightforward, with clear options for updating personal details.
![*Edit-profile*](readme-img/phase_01/edit-profile.png)

___

## Error:
This screen is displayed when a user tries to access a page that does not exist. It features a clear message indicating that the page is not found and provides options to return to the homepage or contact support for further assistance. The design is simple and user-friendly, ensuring users can easily navigate back to the main site.
![*Error*](readme-img/phase_01/error.png)

___

## Faqs:
This screen provides answers to frequently asked questions about the restaurant, including reservations, menu options, allergens, and more. It also includes a section for customer testimonials, highlighting positive experiences from various patrons. The interface is informative and well-organized, making it easy for users to find the information they need. Contact details and opening hours are also provided for quick reference.
![*Faqs*](readme-img/phase_01/faqs.png)

___

## Menu:
This screen displays the food menu of Voltereta Croqueta, allowing users to search for their favorite dishes. It features a variety of dishes categorized by type, such as breakfast and dinner, with brief descriptions for each. A "VIEW MORE" option suggests additional dishes are available.
![*Menu*](readme-img/phase_01/menu.png)

___

## New-dish:
This screen allows administrators to add a new dish to the menu. It includes fields for the dish name, description, price, and ingredients. There are also options to select allergens and dietary preferences, such as vegan options. An upload feature for dish images is included. The "ADD DISH" button finalizes the addition.
![*New-dish*](readme-img/phase_01/new-dish.png)

___

## Order-confirmation:
This screen confirms that a user's order has been successfully placed. It provides a thank-you message and informs the user that they will receive a confirmation email shortly. A "VIEW LAST ORDERS" button allows users to check their orders list.
![*Order-confirmation-screen*](readme-img/phase_01/order-confirmation.png)

___

## Order-manager:
This screen allows administrators to manage restaurant orders. It displays a list of current orders with details such as order number, date, total amount, and ordered items. Each order includes options to view more details, cancel, or accept the order. A progress tracker shows the number of active orders and the order limit. The interface is designed for easy order monitoring and management.
![*Order-manager*](readme-img/phase_01/order-manager.png)

___

## Order-summary:
This screen provides users with a detailed summary of their order before proceeding to payment. It displays the list of ordered items, their quantities, individual prices, and the total cost, including delivery fees. The selected delivery address is also shown. Users have the option to go back, cancel the order, or proceed to payment. The interface ensures clarity and ease of navigation for a smooth checkout experience.
![*Order-summary*](readme-img/phase_01/order-summary.png)

___

## Order-summary-admin:
This screen is designed for restaurant administrators to review and manage customer orders. It displays a detailed summary of each order, including the order number, list of purchased items with their quantities, individual prices, and the total cost, including delivery fees. Additionally, it provides customer information, such as name and delivery address, as well as the restaurant location associated with the order.

Admins can also see whether the order is for pickup or delivery, with an option to modify this selection. A "Back" button allows them to return to the order management interface. The layout ensures clarity and ease of navigation, helping staff efficiently process and verify orders.
![*Order-summary-admin*](readme-img/phase_01/order-summary-admin.png)

___

## Orders-list:
This screen allows customers to view their past orders. It displays order details, including the date, order number, and total price. Each order card lists the ordered items, their quantities, and individual prices. Customers can access additional options, such as viewing more details, downloading the receipt, reordering, or rating their experience.
![*Orders-list*](readme-img/phase_01/orders-list.png)

___

## Pickup-delivery-order:
This screen allows customers to choose between pickup and delivery options for their orders. Users can select their preferred order type and fill out the necessary details, such as location, name, and time for pickup or address and contact information for delivery. A simple interface ensures an easy and efficient ordering process, with a "Confirm" button to finalize the selection.
![*Pickup-delivery-order*](readme-img/phase_01/pickup-delivery-order.png)

___

## Profile:
This screen displays the user profile information for Voltereta Croqueta. It includes essential details such as name, email, phone number, and address, allowing users to review their personal data. The interface offers quick access to actions like editing the profile, viewing past orders, or returning to the homepage. A clean layout ensures easy navigation for managing account details.
![*Profile*](readme-img/phase_01/profile.png)

___

## Rate-order:
This screen allows users to view and rate their past orders. It includes details like the order number, date, and the dish's name with a description and price. Users can provide feedback by rating their dish with a star-based system and submitting their review. A "Back to Order" button ensures easy navigation to previous orders. The clean and structured layout ensures a user-friendly experience.
![*Rate-order*](readme-img/phase_01/rate-order.png)

___
## Ban-message:
This screen displays a notification to the user, JohnDoe123, informing them that their account has been suspended due to violations of the platform's terms of service. The message is clear and direct, providing the user with the option to contact the support team if they believe the suspension is a mistake. The "CONTACT SUPPORT" button is prominently displayed, guiding the user on the next steps to resolve the issue.
![*Ban-message*](readme-img/phase_01/ban-message.png)

___

## Manage-user:
The User Management screen provides administrators with a comprehensive overview of all registered users on the Voltereta Croqueta. It features a search bar for quick user lookup and a table displaying user details, including ID, Username, Email, Status (Active or Banned), and Actions (represented by icons for managing user accounts). The interface is designed for efficiency, allowing admins to easily monitor and manage user statuses and perform necessary actions such as banning or unbanning users.
![*Manage-user*](readme-img/phase_01/manage-user.png)

___

## Restaurant-availability:
This screen provides an overview of the reservation status for different restaurants of Voltereta Croqueta. It includes a table that lists the restaurant, date, total reservations, available seats, and the current status (Available or Full). Each entry also has an option to "View Details" for more information. The interface is designed to help restaurant staff quickly assess availability and manage reservations efficiently.
![*Restaurant-availability*](readme-img/phase_01/restaurant-avaliability.png)

___

## Restaurant-information:
This screen provides essential details about Voltereta Móstoles, including its address, contact information, and opening hours. Users can explore available services such as accessibility, WiFi, and delivery. The page also features booking and ordering options, along with images of the restaurant and its location on Google Maps.
![*Restaurant-information*](readme-img/phase_01/restaurant-information.png)

___

## Restaurants:
This screen allows users to explore the various locations of Voltereta Croqueta. It features a search bar for finding nearby restaurants and a "Use My Location" button for convenience. Users can view popular restaurants through images and interact with an embedded Google Maps preview for easy navigation.
![*Restaurants*](readme-img/phase_01/restaurants.png)

___
# :diamonds: Flow Diagram:

The diagram is a flow chart showing user permissions and navigation steps within a web interface. The diagram uses colored arrows to indicate the progression from one web page or state to another, based on user actions or permissions.

- The diagram is a complex schematic with multiple screenshots of web pages connected by colored arrows indicating different user actions or states.
- Each screenshot represents different sections or pages of a restaurant website.
- There is an initial state at the top: "Index", leading to different paths depending on the user's status.
- Arrows connect these initial state to various web pages, including "Menu", "Restaurants", "Profile" and diferent admin workflow pages, among others.
- Each colored arrow represents a user, and therefore the path through which he/she can navigate the site.
- Red arrows represent Admin functions, the green ones are for Registered User and the blue arrows are for every user. 

![*Flow Diagram*](readme-img/phase_01/flow_diagram.png)
___
# Phase 2
___
#  :computer: New Screens
___
## Booking management:
This image shows the Booking Management Panel for Voltereta Croqueta, where admins can view and manage reservations. It includes a search bar, a reservation list with details (user, restaurant, shift, people, and date), and a delete button for easy management.

![*Booking_management.png*](readme-img/phase_02/Booking_management.png)


___

## Dish management:
This image displays the Dish Management Panel for Voltereta Croqueta, where admins can manage the restaurant's menu. It includes a search bar, a list of dishes with their prices, and edit/delete buttons for easy modifications.
![*Booking_management.png*](readme-img/phase_02/Dish_management.png)



___
## User management:
This image showcases the User Management Panel for Voltereta Croqueta, allowing admins to manage user accounts. It includes a search bar, a list of users with emails and statuses, and ban/unban buttons for controlling access.
![*Booking_management.png*](readme-img/phase_02/User_management.png)



___
## Admin Dish Info:
This screen displays detailed information about a specific dish, including its name, description, price, ingredients, and allergens. Admins can edit, mark as unavailable, or delete the dish.
![*AdminDishInfo.png*](readme-img/phase_02/AdminDishInfo.PNG)



___
## Admin Edit Dish:
This image showcases the interface for editing dish details. Admins can modify the dish name, description, price, ingredients, and allergens before saving changes.
![*Admin-EditDish.png*](readme-img/phase_02/Admin-EditDish.PNG)



___
## Admin New Dish:
This screen provides a form for adding a new dish to the menu. Admins can input dish details, select allergens, dietary preferences, and upload an image.
![*Admin-NewDish.png*](readme-img/phase_02/Admin-NewDish.PNG)



___
## Booking:
This screen presents the reservation form, enabling users to select a restaurant, date, shift, and number of people, while providing personal details to confirm the booking.
![*Booking.png*](readme-img/phase_02/Booking.PNG)



___
## Existing Reservation:
This image shows the notification for users who already have an active reservation, preventing duplicate bookings and directing them to manage their reservation in the profile section.
![*ExistingReservation.png*](readme-img/phase_02/ExistingReservation.PNG)



___
## Booking Cancelled:
This confirmation screen informs users that their reservation has been successfully cancelled, offering a direct link to rebook a table.
![*BookingCancelled.png*](readme-img/phase_02/BookingCancelled.PNG)



___
## Profile:
This screen displays user profile details, including personal information and an active reservation summary, with the option to cancel the booking directly from the page.
![*Profile.png*](readme-img/phase_02/Profile.PNG)



___
## Restaurants:
This page lists available restaurant locations, providing images and addresses, along with a search bar to filter by city or postal code.
![*Restaurants.png*](readme-img/phase_02/Restaurants.PNG)



___
## Admin Dashboard:
This panel serves as the main admin interface, allowing access to various management functionalities such as user administration, dish control, and booking supervision.
![*AdminDashboard.png*](readme-img/phase_02/AdminDashboard.PNG)



___
## Confirm Action Popup
This modal appears when performing critical actions, such as deleting a reservation, requiring users to confirm before proceeding.
![*ConfirmActionPopUp.png*](readme-img/phase_02/ConfirmActionsPopUp.PNG)
___
# :diamonds: New Flow Diagram:

The diagram is a flow chart showing user permissions and navigation steps within a web interface. The diagram uses colored arrows to indicate the progression from one web page or state to another, based on user actions or permissions.

![*New Flow Diagram*](readmeImg/diagramFinal.jpg)
# :rocket: Execution Instructions

##  Steps
1. **Download the Repository**


2. **Check Requirements:**
- Java JDK 19
- MySQL v.8.0.36.0
- Maven 4.0.0
- Spring Boot v3.4.2
- Integrated Development Environment (IDE) - IntelliJ IDEA recommended

3. **Configure Database:**
- Download MySQL v.8.0.36.0
- Select default port (port 3306)
- Create a user with name root "root" and password "webapp07" with DB admin as user role
- Configure MySQL Server as Windows Service
- Grant full access to the user

4. **Docker Configuration:**
- Download Docker desktop.
- Execute in terminal:
  - docker run -d --name volteretaCroquetaBD -p 3306:3306 -e MYSQL_ROOT_PASSWORD=webapp07 -e MYSQL_DATABASE=volteretaCroqueta mysql:latest
  - docker exec -it volteretaCroquetaBD bash

5. **Configure IDE:**
- Install IntelliJ IDEA (or your preferred IDE).
- Install Maven and Spring plugins for your IDE.

6. **Run Application in the IDE:**
- Open the project in your IDE.
- Build the project using Maven.
- Run the application.

7. **Access the Application:**
- Visit https://localhost:8443 in your web browser.

--- 
# :file_folder: Diagram with the database entities

Next, a diagram will be included depicting the entities within the database, their respective fields, and the relationships among them.


![*Database*](readmeImg/database.jpg)

# :art: Class diagram and templates
![*templateDiagram*](readmeImg/templateDiagram.JPG)
---
# :raised_hand: Participation

---


#### Nicolás Hernández Tejero

*  Implemented error handling across the application to improve reliability.
*  Added Javadocs for better code documentation and maintainability.
*  Developed reusable Navbar, Header, and Footer components for consistent UI.
*  Created Admin Panel to manage users and dishes efficiently.
*  Designed a confirmation modal for critical user actions.
*  Implemented show and edit profile functionality.
*  Added ban/unban functionality for user management.
*  Secured authentication for all views, ensuring restricted access based on roles.
*  Developed login and registration functionality for new users.
*  Strengthened security in the login process to prevent unauthorized access.
*  PDF generation

| #   |                                                                                    Commit                                                                                     |     | #   |                                                                                                             File                                                                                                              |
| :-: |:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------:| :-: | :-: |:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| 1º  |                      [fix: login spring boot problem](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/0ceeb4baf48c277fec7645962143363d2c6e0297)                       | | 1º |                               [ProfileController.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/controller/ProfileController.java)                                |
| 2º  |        [refactor: unify profile view and edit into single template](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/1ca74809847b20f93aaea0746d5f323fd77239b4)         | |2º |                                  [AuthController.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/controller/AuthController.java)                                   |
| 3º  |           [feat: add authentication interceptor and admin panel](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/a686f202d534e96e771427b98d2a7982190a0ca7)            | | 3º |                                 [AuthInterceptor.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/interceptor/AuthInterceptor.java)                                 |
| 4º  |          [feat(admin): Add ban and unban functionality for users](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/58c1220ee0c621537c56745e2500d2ef526d8153)           | | 4º |               [CustomAuthenticationFailureHandler.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/security/CustomAuthenticationFailureHandler.java)                |
| 5º  |                   [feat: add reusable confirmation modal](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/f2a56d57152ea931495cdb001b4f8dce7a57ac29)                   | | 5º |                                   [UserService.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/service/UserService.java)                                   |

  #### Víctor Muñoz Regalón

*  Developed the **booking system** for users, allowing them to make, view, and cancel reservations.
*  Ensured that users can only have one **active reservation** at a time to prevent duplicate bookings.
*  Implemented **real-time availability checks** for restaurant reservations based on date, shift, and capacity.
*  Created the **booking confirmation** and **cancellation views**, improving user experience.
*  Developed the **admin panel for booking management**, allowing administrators to view and delete the active reservations.
*  Integrated **AJAX-based** dynamic seat availability updates when users select a restaurant and shift, allowing real-time updates without reloading the page.
*  Implemented **flash messages** for errors and confirmations in booking actions.
*  Developed the **profile page integration** to display the user’s current reservation with a cancel button.
*  Designed and implemented the **database structure for bookings**, linking users and restaurants.
*  Ensured that users can only make reservations for **available seats** to prevent overbooking.


| #   |                                                                         Commit                                                                          |     | #   |                                                                                       File                                                                                       |
| :-: |:-------------------------------------------------------------------------------------------------------------------------------------------------------:| :-: | :-: |:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| 1º  |                [Update: Booking.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/08e53c5923bbea164a9f8a31493fe47e90a3e925)                 | | 1º |                     [Booking.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/model/Booking.java)                      |
| 2º  |               [Add: BookingController](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/5815625ddf1b64f270058b67f1ff7e89034ccb4b)                | |2º |         [BookingController.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/controller/BookingController.java)         |
| 3º  |                 [Add: BookingService](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/067a2e4ea70f90dffe4df3ff75e97248984cee65)                 | | 3º |             [BookingService.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/service/BookingService.java)              |
| 4º  | [Update booking.html: Add Javascript available seats](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/71d3945bea29478148564cbba9eac86b47baeedc) | | 4º |                           [Booking.html](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/resources/templates/booking.html)                           |
| 5º  |            [Fixed: AdminBookingController](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/63e26cd149b99b2af1513426b6d9f234359752f9)            | | 5º | [AdminBookingController.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/controller/admin/AdminBookingController.java) |

#### Hugo Sánchez Torres

*  Developed the application's **dish menu**.
*  Implemented dynamic data loading based on **AJAX**, taking filter values into account.
*  Developed the functionality to **edit**, **add**, and **delete** dishes.
*  Implemented a **filter fragment** for the application's menu.
*  Developed a **filter** for the dish menu, with the ability to filter by **name**, by an **ingredient**, or by **maximum price**.
*  Mark **dishes as disabled**.
*  Added **image implementation** using Blob.


| #   |                                                              Commit                                                               |     | #   |                                                                                       File                                                                                       |
| :-: |:---------------------------------------------------------------------------------------------------------------------------------:| :-: | :-: |:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| 1º  |        [Fix: Menu's Ajax](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/111fd36026297d408610741d3eeefc7ab3dfbf6a)        | | 1º |                       [dishes.js](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/model/Booking.java)                       |
| 2º  |    [Fix: Add and edit dishes](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/d9e3df83d2eabe4f2987fade0d8bebde66bfe63d)     | |2º |         [BookingController.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/controller/BookingController.java)         |
| 3º  |      [Update: Filter done](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/25236b83f1a2d05165be799cdb5f7f22afeebadb)      | | 3º |             [BookingService.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/service/BookingService.java)              |
| 4º  |      [Fix: dish image format](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/d372ae74771132b8229c6ae4ca9578c0207842e2)      | | 4º |                           [Booking.html](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/resources/templates/booking.html)                           |
| 5º  | [Fixed: AdminBookingController](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/63e26cd149b99b2af1513426b6d9f234359752f9) | | 5º | [AdminBookingController.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/blob/main/backend/src/main/java/es/codeurjc/backend/controller/admin/AdminBookingController.java) |


 #### Juan Ramírez Blanco

* Developed the **order system architecture**, ensuring a structured and scalable approach for managing orders.
* Created the **Order Summary screen**, displaying a detailed breakdown of the user's order before payment.
* Helped in the implementation of the **Add to Cart button**, allowing users to add items to their cart dynamically.
* Implemented the **Pay button**, allowing users to securely process their payments.
* Designed and developed the **order confirmation** and **success screens**, enhancing the user experience after a purchase.
* Implemented **View Orders button**, enabling users to access their order history.
* Created **Order History** section, displaying a comprehensive list of paid orders.
* Developed the **class diagram and templates**, providing a structured foundation for order management and UI consistency.


| #   |                                                              Commit                                                               |     | #   |                                                                                       File                                                                                       |
| :-: |:---------------------------------------------------------------------------------------------------------------------------------:| :-: | :-: |:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| 1º  |        [Add: Order Controller.java](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/b8836003082903b60ddd5ce191acf4b6f3434d72)        | | 1º |                       [Controller.java]()                       |
| 2º  |    [Add: add cart](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/14d1bda382c3e135ac5d422e036e32fe35a177b4)     | |2º |         [OrderController.java]()         |
| 3º  |      [Add: user order history](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/870e86495948e0ee9fa24915fa667d7c8f96b4c5)      | | 3º |             [OrderController.java]()              |
| 4º  |      [Fix: More Info Button in OrderSummary](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/14d1bda382c3e135ac5d422e036e32fe35a177b4)      | | 4º |                           [OrderController.java]()|
| 5º  | [Add: Order summary pay button and confirm page](https://github.com/CodeURJC-DAW-2024-25/webapp07/commit/13fd26bae5554d4e254992baaa8c09c7766d5e58) | | 5º | [OrderController.java]() |
