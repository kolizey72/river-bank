# River Bank
## About
This project is a mock web application of non-existent bank. 
It implements basic banking operations, such as deposit, withdraw and transfer. 
All operations are saved and can be browsed anytime.
## Used technologies
- Java
- Spring Framework(MVC, Boot, Security, Data JPA)
- Hibernate ORM
- SQL, PostgreSQL
- Thymeleaf
- HTML/CSS, Bootstrap

## Interaction with application
Website available in two languages: English and Russian. You can easily switch between them anytime in footer section. 

### Registration and authentication 
Authentication in River Bank is an implementation of Spring Security.

Registration uses DTO and is validated via Hibernate Validator, custom Spring Validator.

Requirements: 
* Email(valid, unique)
* Name(size: 3-32, unique),
* Password(min: 8),
* Confirm password(same as password)

Logging in requires user to enter email and password. Remember me check is not represented.

### Accounts
On profile page user can see all available accounts and open new, if not reached limit. Default limit is 5 account per user.

On account information page user can perform deposit and withdraw operations, access transfer page and operations history and delete an account.
Deletion requires setting account balance to 0.

### Transfer
On transfer page user can perform a transfer from any of their account to any other account(including other user's accounts).

Important note: currency exchange is not available yet, to trying to commit a transfer between accounts with different currencies will lead to nothing.

### Operations
There are four available operations: creation of an account, deposit, withdraw and transfer. All of them are saved in database. 
User can see their operation history or operation history of any account of them.

## Hosting
Demonstration of this application available [here](https://river-bank.up.railway.app).
