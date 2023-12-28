# API Endpoints

## Group Operations

### Get Group List
- **URL:** http://localhost:8081/httpmethod/group-list/1
- **Method:** GET
- **Description:** Retrieve the list of groups.

### Get Group Details
- **URL:** http://localhost:8081/httpmethod/group-details/5
- **Method:** GET
- **Description:** Get details of a specific group.

### Update Group
- **URL:** http://localhost:8081/httpmethod/update/group/2
- **Method:** PUT
- **Description:** Update information for a group.

### Get Group Members
- **URL:** http://localhost:8081/httpmethod/group-member/1
- **Method:** GET
- **Description:** Retrieve members of a group.

### Add Group
- **URL:** http://localhost:8081/httpmethod/add/group/1
- **Method:** POST
- **Description:** Add a new group.

## User Operations

### Get User List
- **URL:** http://localhost:8081/httpmethod/user-list
- **Method:** GET
- **Description:** Retrieve a list of all users.

### Get User Details
- **URL:** http://localhost:8081/httpmethod/user-details/2
- **Method:** GET
- **Description:** Get details of a specific user.

### Register User
- **URL:** http://localhost:8081/httpmethod/register/1
- **Method:** POST
- **Description:** Register a new user.

### Login
- **URL:** http://localhost:8081/httpmethod/login
- **Method:** POST
- **Description:** User login.

### Logout
- **URL:** http://localhost:8081/httpmethod/logout
- **Method:** POST
- **Description:** User logout.

## Expense Operations

### Add Expense
- **URL:** http://localhost:8089/httpmethod/add/expense
- **Method:** POST
- **Description:** Add a new expense.

### Filter Amount Summary
- **URL:** http://localhost:8089/httpmethod/filter-amount/1
- **Method:** GET
- **Description:** Filter and retrieve a summary report for a user or group.

### Expense List - Group
- **URL:** http://localhost:8089/httpmethod/expense-list/1
- **Method:** GET
- **Description:** Retrieve the list of expenses for a group.

...

## Notification Operations

### Notification List - User
- **URL:** http://localhost:8085/httpmethod/notify-list/1
- **Method:** GET
- **Description:** Retrieve notifications for a specific user.

### Add Notification
- **URL:** http://localhost:8085/httpmethod/add/notification
- **Method:** POST
- **Description:** Add a new notification.

### Read Notification
- **URL:** http://localhost:8085/httpmethod/notify-read/14
- **Method:** PUT
- **Description:** Mark a notification as read.

...

## Miscellaneous Operations

### Filter by Category in Specific Group
- **URL:** http://localhost:8089/httpmethod/filter-group-category?groupId=1&category=Trip
- **Method:** GET
- **Description:** Filter by category in a specific group.

### Summary by Category
- **URL:** http://localhost:8089/httpmethod/filter/user-category/2
- **Method:** GET
- **Description:** Retrieve a summary by category.

### Filter by Group Name
- **URL:** http://localhost:8081/httpmethod/filter-group/On Road
- **Method:** GET
- **Description:** Filter by group name.

...

## Non-Group Expense Operations

### Non-Group Expense List
- **URL:** http://localhost:8089/httpmethod/non-group-expenses/1/3
- **Method:** GET
- **Description:** Retrieve a list of non-group expenses.

### Add Non-Group Expense
- **URL:** http://localhost:8089/httpmethod/add/non-group-expense
- **Method:** POST
- **Description:** Add a new non-group expense.

### Payment in Non-Group Expense
- **URL:** http://localhost:8089/httpmethod/non-group-pay/bb60ab86-59cf-fbbc-32a3-2157ae9a5d95/3
- **Method:** PUT
- **Description:** Process payment in a non-group expense.

...

## User-Specific Operations

### User-Specific User List for Non-Group Expense
- **URL:** http://localhost:8081/httpmethod/user-list/2
- **Method:** GET
- **Description:** Retrieve a user-specific user list for non-group expenses, sorted.

### Filter by Username
- **URL:** http://localhost:8081/httpmethod/filter-user/bA La
- **Method:** GET
- **Description:** Filter by username.

...
