# Student Management System (SMS) — Premium University Admin Dashboard

A production-grade, responsive University Student Management System Admin Dashboard inspired by the **Acadex** academic design language. Built strictly with **Java 21, Spring Boot 3.3.5, Spring MVC, Thymeleaf, Spring Data JPA, PostgreSQL, HTML5, CSS3, JavaScript, and Bootstrap 5**.

---

## 🏛️ Brand Identity & Aesthetics

- **Primary Colors**: Deep Navy (`#172554`), Academic Blue (`#2563EB`), Soft Ice Blue (`#EFF6FF`)
- **Background**: Soft Gray/Blue Canvas (`#F6F8FC`), Pure White Cards (`#FFFFFF`)
- **Status Accents**: Emerald Green (`#16A34A`), Warm Amber (`#F59E0B`), Crimson Red (`#DC2626`), Sky Blue (`#0284C7`)
- **Typography**: Inter / Plus Jakarta Sans
- **Layout**: Fixed/Collapsible Sidebar + Sticky Header + Responsive Main Grid

---

## 🚀 Key Modules & Pages

### 1. Executive Dashboard (`/dashboard`)
- **Institutional Greeting**: "Good Morning, Admin!" banner with live date and primary `+ Add New Student` CTA.
- **4 Key Statistic Cards**:
  - Total Enrolled Students (with growth indicator)
  - Active Academic Courses
  - Today's Attendance Rate
  - Active Departments & Faculties
- **Analytics Visuals**:
  - *Student Enrollment Trends*: Chart.js smooth area curve with dual-year comparison (2024 vs. 2025).
  - *Faculty Distribution*: Chart.js doughnut chart dynamically populated from database student counts.
- **Recent Registrations**: Student table with initials avatar, status badge, and quick action links.
- **Real-time Activity Feed**: Audit timeline recording enrollments, attendance sessions, and syllabus updates.

### 2. Student Directory (`/students`)
- Multi-facet filters: Search by name, ID, or email; filter by Department, Academic Year, and Status; instant Reset button.
- Comprehensive table: Checkbox selection, ID, avatar, contact phone, department, semester, matriculation date, and status badges.
- Modals & Actions: View profile, edit, and permanent delete with a confirmation modal dialog.
- Paginated with responsive Previous / Page numbers / Next navigation.

### 3. Student Registration & Update (`/students/add`, `/students/edit/{id}`)
Divided into 3 logical academic sections:
1. **Personal Information**: Full Name, University Email, Phone, Date of Birth, Gender, Avatar URL.
2. **Academic Details**: Student ID, Department, Enrolled Course, Semester (1-8), Enrollment Date, Academic Year, Status, and GPA.
3. **Residential Address**: Street Address, City, State/Province, PIN/Postal Code.

### 4. Comprehensive Student Profile (`/students/{id}`)
- Hero identity card with large avatar initials, status badge, and student ID.
- Personal information card & contact credentials.
- Academic standing card with Cumulative GPA (out of 4.00) and matriculation details.
- Term attendance summary progress tracker (Days Present vs. Days Absent).
- Enrolled course roster and recent attendance log.

### 5. Course Catalog & Curricula (`/courses`, `/courses/add`)
- Metrics: Total courses, active courses running, and total student enrollments.
- Course roster with course code badges (`CS101`, `CS204`, `IT305`), instructor, term, and maximum capacity.
- Add course modal dialog and dedicated creation page.

### 6. Daily Roll Call & Attendance Tracking (`/attendance`)
- Daily attendance metrics: Total active students, present today, absent today, and overall attendance rate.
- Filtering by date, department, course subject, and semester.
- Status badges: **Present** (Green), **Absent** (Red), **Late** (Amber), and **Leave** (Blue).
- Classroom session roll call modal with **"Mark All Present"** and **"Mark All Absent"** bulk shortcuts.
- **Export Attendance Sheet** (`/attendance/export-csv`): Download instant CSV roll-call spreadsheets.

### 7. Academic Analytics & Reporting (`/reports`)
- Comprehensive student audit roster.
- Weekly attendance stacked breakdown and faculty distribution charts.
- **Export CSV** (`/reports/export-csv`): Full student dataset export.
- **Print View**: Print-ready CSS (`@media print`) that removes navigational sidebars and topbars for physical printing or PDF saving.

### 8. System Preferences & Settings (`/settings`)
- **Admin Profile**: Master administrator information.
- **University Information**: Campus name, accreditation code, and registrar office contact.
- **Security & Password**: Change administrator password with confirmation matching.
- **System Preferences**: Daily roll-call email summaries and low attendance alerts (< 75%).

### 9. Secure University Portal Login (`/login`)
- Split-screen layout: Clean credentials form on the left; university enterprise visual showcase on the right.
- Password show/hide toggle (`bi-eye`), remember-me check, and error handling.

---

## 🛠️ How to Run

### Development Mode (Embedded In-Memory H2 Database)

The default development profile uses an in-memory H2 database, so a separate database server is not required. Sample data is seeded automatically when the application starts.

1. Install **Java 21** (or newer) and ensure `java -version` works in Terminal.
2. Open Terminal and change to the project folder:
   ```bash
   cd /Users/tanmayburoy/Desktop/student-management-system
   ```
3. Start the application with the included Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   On macOS, if the wrapper is not executable, run `chmod +x mvnw` once, then retry.
4. Open the site at **http://localhost:8080**. Keep the Terminal process running while using the site; press `Ctrl+C` to stop it.

The H2 Console is available at **http://localhost:8080/h2-console** (JDBC URL: `jdbc:h2:mem:smsdb`, username: `sa`, leave the password blank).

#### If the Tomcat welcome page appears at `localhost:8080`

The Apache Tomcat welcome page means the browser reached a separate Tomcat server; it is not this Spring Boot application. First check which process owns port `8080`:

```bash
lsof -nP -iTCP:8080 -sTCP:LISTEN
```

If it shows standalone Tomcat, stop that Tomcat from the Terminal where it was started (usually `Ctrl+C`) or stop its service. Then, from this project folder, run:

```bash
./mvnw spring-boot:run
```

If you started Tomcat from IntelliJ, click its red **Stop** button in the Run/Services window. If you cannot find its Terminal or IDE window, open macOS **Activity Monitor**, search for `java`, select the Tomcat process, and stop it. (The `lsof` command above shows its PID; use Activity Monitor to verify which Java process it is before stopping it.)

Then, from this project folder, run `./mvnw spring-boot:run`. Wait until the output says `Tomcat started on port 8080` and `Started StudentManagementApplication`. Then open **http://localhost:8080/login**. A Tomcat 404 page saying `The requested resource [/login] is not available` means standalone Tomcat is still answering; it does not indicate that this application has started. If startup fails, resolve the Terminal error first; refreshing the browser will continue to show whichever server owns the port.

If you need to leave standalone Tomcat running, launch this Spring Boot app on port `8081` with:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

Wait for the startup output to say `Tomcat started on port 8081`, then open **http://localhost:8081/login**. Port `8081` will not serve this app unless it is started with this port setting. To make `8081` the default instead, change `server.port=8080` to `server.port=8081` in `src/main/resources/application.properties` and restart the app.

### Production Mode (PostgreSQL Database)

1. Install and start PostgreSQL, then create the database:
   ```sql
   CREATE DATABASE sms_db;
   ```
2. Update the connection settings in `src/main/resources/application-postgres.properties` if needed.
3. Start the app from the project folder:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
   ```

### Building Executable JAR
```bash
./mvnw clean package
java -jar target/student-management-system-1.0.0.jar
```
