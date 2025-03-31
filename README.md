# 🎬 MovieLand_App

**MovieLand_App** is a full Java-based client-server movie catalog application that allows users to search, add, update, and remove movies through a friendly graphical interface. The system is divided into two main components: a backend server and a JavaFX-based frontend client.

---

## 📌 Project Structure

- `movieApp/` – Backend server: handles logic, data storage (in-memory), services, and request parsing.
- `GUI_MovieApp/` – Frontend client: built with JavaFX, provides an interactive UI for searching and managing the movie catalog.

---

## 🧠 Key Features

- 🔍 **Advanced Movie Search**: Users can search by title, genre, actor, or director using efficient string matching algorithms.
- 🎞️ **Movie Management (Admin)**: Add, update or delete movie records using a simple UI (admin view).
- 👥 **Admin Management (Super Admin)**: Manage system administrators via role-based access (only available to a "super-admin").
- 🌐 **Client-Server Architecture**: Based on JSON messages over sockets, cleanly separates logic and presentation.

---

## 🧮 Algorithms
The project includes two string search algorithms to improve query efficiency:

### 🔹 KMP (Knuth–Morris–Pratt): 
Efficient for pattern matching within strings by preprocessing the pattern to avoid unnecessary comparisons.


### 🔹 Rabin-Karp
Used for fast pattern matching in movie searches (title, actor, genre, etc.). Enables efficient substring matching within large datasets.

#### ✨ How it works in the project:
When a user types a query (e.g., “Nolan”), the system uses Rabin-Karp to match it against relevant movie fields and returns accurate results in real-time.

---

## ⚙️ Technologies Used

| Layer     | Technologies |
|-----------|--------------|
| Backend   | Java, JSON (Gson), Socket Programming |
| Frontend  | JavaFX, SceneBuilder |
| Tools     | Git, GitHub, JUnit, IntelliJ |

---

## 🧪 Testing

Unit testing is performed using **JUnit 5**:
- `MovieServiceTest`: tests adding, updating, deleting, and retrieving movies.
- `AdminServiceTest`: validates all admin-related functionalities.
- All edge cases such as empty inputs or invalid updates are tested.

---

## 🧩 How It Works

### 🖥️ Client Side (GUI)
1. User performs an action (e.g., search or add movie).
2. A `Request` object is built and converted to JSON.
3. JSON is sent to the server using `Client.java`.

### 🖧 Server Side
1. `Server.java` receives the request.
2. `HandleRequest.java` parses it, identifies the action (e.g., `movie/search`).
3. Action is routed to the relevant service (e.g., `MovieService`).
4. Response is returned and parsed by the client using `ResponseParser`.

---

## 📷  Demo

🎥 Watch the full demo: [Click here](https://youtu.be/2SHQ0WDwKBw)


---

## 🔐 Roles

- **Client** – Regular user searching for movies.
- **Admin** – Can manage movie records.
- **Super Admin** – Can manage other admins (view, add, delete).

---

## 🚀 Getting Started

1. Clone the repo:
```bash
git clone https://github.com/<your-username>/MovieLand_App.git
