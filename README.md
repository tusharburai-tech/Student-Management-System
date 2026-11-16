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

### Connecting to Supabase (Cloud PostgreSQL)

Supabase provides a free, hosted PostgreSQL database in the cloud with a built-in Table Editor UI.

1. **Create a Supabase Project:**
   - Go to [supabase.com](https://supabase.com) and create a free project.
   - Save your database password securely.
2. **Get Connection Details:**
   - In Supabase, go to **Project Settings** → **Database** → **Connection string** → Select **JDBC**.
   - The JDBC URL format looks like:
     `jdbc:postgresql://aws-0-[region].pooler.supabase.com:6543/postgres?sslmode=require` (or direct: `jdbc:postgresql://db.[ref].supabase.co:5432/postgres?sslmode=require`)
3. **Configure Environment Variables (in Render or locally):**
   - `SPRING_PROFILES_ACTIVE`: `postgres`
   - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://<HOST>:<PORT>/postgres?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME`: `postgres.<PROJECT_REF>` (or `postgres`)
   - `SPRING_DATASOURCE_PASSWORD`: `<YOUR_SUPABASE_PASSWORD>`
4. Hibernate automatically creates all tables on startup. You can also view or run `supabase-schema.sql` in the Supabase SQL Editor.

### Deploy to Google Cloud Run from GitHub

This project can be built as a Java application from its GitHub repository and deployed as a Cloud Run web service. The repository root must contain `pom.xml`. The app reads Cloud Run's `PORT` environment variable and defaults to port `8080` for local development.

> **Billing requirement:** Google Cloud requires a billing account and supported payment method for a standard Cloud Run deployment. If account setup is blocked at payment verification, ask your school for a Google Cloud project or temporary Skills Boost lab access before continuing.

#### 1. Prepare and push the project to GitHub

1. Push the project to a GitHub repository. A private repository is supported; you will authorize Google's Cloud Build GitHub app to access it in the next section.
2. Make sure the latest commit is on the branch you plan to deploy (for example, `main`) and that `pom.xml` is at the repository root.
3. Do not commit `target/`, `.idea/`, or database passwords. The included `.gitignore` excludes generated and IDE files.

#### 2. Create the Cloud Run deployment from the GitHub repository

1. In Google Cloud Console, select or create a project and complete billing setup.
2. Open **Cloud Run** and select **Create service** / **Connect repository**.
3. Choose **Cloud Build**, connect GitHub, authorize access to this private repository, then select the repository and deployment branch.
4. Configure the build to use the repository root and Java buildpacks (the root `pom.xml` identifies this as a Maven Java application; a Dockerfile is not required for source deployment).
5. Choose a region close to your users, such as Mumbai (`asia-south1`), and configure the service. Enable public access only if you want anyone with the URL to reach the login page.
6. If prompted, enable Cloud Run, Cloud Build, and Artifact Registry APIs. Start the deployment and wait for the build and service health checks to finish.

Google's source deployment uses Cloud Build to build and deploy the Java service. After the first deployment, the connected repository can be configured to build and deploy new commits automatically. See [Deploy services from source](https://docs.cloud.google.com/run/docs/deploying-source-code) and [continuous deployment from a repository](https://docs.cloud.google.com/run/docs/continuous-deployment).

#### 3. Open the deployed website

Open the service URL shown in Cloud Run and append `/login`, for example:

```text
https://YOUR-SERVICE-URL.run.app/login
```

The login page currently displays demo administrator credentials. Change or remove these demo credentials before making the service public.

#### Database behavior and persistent storage

The default profile uses an in-memory H2 database, so this is suitable for an initial demo but student data can be lost when a Cloud Run instance stops or restarts. Cloud Run instances are not a persistent place to store database files. For data that must remain available, create a Cloud SQL for PostgreSQL instance, attach it to the Cloud Run service, add the Cloud SQL Java Connector dependency, and configure the PostgreSQL JDBC URL, username, and password through Cloud Run environment variables or Secret Manager. Do not activate the current `postgres` profile in the cloud unchanged: `application-postgres.properties` points to `localhost` and contains local example credentials. Follow Google's [Cloud Run to Cloud SQL guide](https://docs.cloud.google.com/sql/docs/postgres/connect-run) for the connector and connection setup.

Cloud SQL is a separate billable service. Review current pricing and clean up unused Cloud Run services and databases when you finish experimenting.

### Building Executable JAR
```bash
./mvnw clean package
java -jar target/student-management-system-1.0.0.jar
```
