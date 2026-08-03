# Computer Seekho

A full-stack admissions, enquiry-tracking, and fee-management platform built for **USM's Vidyanidhi Info Tech Academy (VITA)** — a computer training institute in Juhu, Mumbai. It covers the full student lifecycle: public course browsing → enquiry → follow-up → admission → installment-based fee collection → receipts.

**Stack:** React (Vite) frontend · Spring Boot REST API backend · MySQL · Razorpay for online payments · JWT-based admin auth

---

## Features

### Public site
- Browse programs, grouped and filterable by age group (School students / College students / Professionals)
- Program detail pages — syllabus, fees, upcoming batches, FAQs
- Placements & recruiters showcase
- Campus gallery and staff directory
- Enquiry form and contact form
- Public fee-payment page (Razorpay)

### Admin panel
- **Dashboard** — open enquiries, today's follow-ups, total admissions
- **Enquiries** — Kanban-style pipeline (New → Contacted → Follow-up due → Registered/Closed), follow-up logging, auto-assignment
- **Admissions** — search an enquiry or an existing student, register a student, and collect fees
- **Installment payments** — pay the full course fee up front or in any number of partial installments, online (Razorpay) or offline (Cash/Cheque/DD/Bank Transfer), with a live running balance
- **Manage Student** — look up any admitted student by name, mobile, student ID, or admission ID; view their full payment history and collect further installments
- **Receipts** — dedicated printable/downloadable (PDF) receipt per payment, with an itemized payment history
- **Content Manager** — courses, batches, staff, albums/photos, announcements, recruiters, placed students
- **Excel bulk upload** — import students/courses in bulk from a spreadsheet

---

## Project structure

```
├── BackEnd/                # Spring Boot REST API
│   └── src/main/java/com/example/
│       ├── controllers/     # REST endpoints
│       ├── services/        # business logic
│       ├── repositories/    # Spring Data JPA
│       ├── entities/        # JPA entities
│       ├── dto/              # request/response DTOs
│       ├── security/         # JWT auth filter/service
│       └── config/            # CORS, Razorpay, security config
│
└── frontend/                 # React (Vite) SPA
    └── src/
        ├── api/               # fetch client + endpoint wrappers
        ├── components/        # layout + shared UI
        ├── context/           # auth context
        ├── pages/
        │   ├── public/        # marketing site
        │   └── admin/         # admin panel
        └── styles/
```

---

## Prerequisites

- Java 17+
- Node.js 18+
- MySQL 8+
- A [Razorpay](https://razorpay.com) account (test mode is fine for development)

---

## Backend setup

1. Create the database:
   ```sql
   CREATE DATABASE Computer_Seekhodb;
   ```
2. Copy `src/main/resources/application.properties` and fill in your own values (or set the equivalent environment variables — see table below). `spring.jpa.hibernate.ddl-auto=update` will create the schema automatically on first run.
3. Run it:
   ```bash
   cd BackEnd
   mvn spring-boot:run
   ```
   The API starts on `http://localhost:8080`.

### Environment variables

| Variable | Purpose | Local default |
|---|---|---|
| `DB_URL` | MySQL JDBC URL | `jdbc:mysql://localhost:3306/Computer_Seekhodb` |
| `DB_USERNAME` / `DB_PASSWORD` | MySQL credentials | `root` / `root` |
| `JWT_SECRET` | Base64 secret used to sign admin login tokens | — |
| `RAZORPAY_KEY_ID` / `RAZORPAY_KEY_SECRET` | From your Razorpay dashboard (Test Mode → API Keys) | — |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | SMTP sender for admission-confirmation emails (Gmail needs an [App Password](https://myaccount.google.com/apppasswords), not your normal password) | — |
| `ALLOWED_ORIGINS` | Comma-separated frontend origin(s) allowed by CORS | `http://localhost:3000` |
| `PORT` | Port the server binds to (used in production hosts like Render) | `8080` |

> ⚠️ Never commit real secrets. If `application.properties` contains real keys, add it to `.gitignore` and use environment variables in any shared or deployed environment.

---

## Frontend setup

```bash
cd frontend
npm install
npm run dev
```
Runs on `http://localhost:3000`.

### Environment variables

| Variable | Purpose | Local default |
|---|---|---|
| `VITE_API_BASE_URL` | Base URL of the backend API | `http://localhost:8080` |

Set this in a `.env` file locally, or as a build-time environment variable on your hosting platform (e.g. Vercel/Netlify).

---

## Building for production

```bash
# Backend
cd BackEnd
mvn clean package
java -jar target/*.jar

# Frontend
cd frontend
npm run build   # outputs to dist/
```

A `Dockerfile` is included for the backend if you're deploying to a container-based host (Render, Railway, Fly.io, etc.).

---

## Tech notes

- **Auth:** JWT issued on admin login (`/api/auth/login`), attached as a `Bearer` token on subsequent requests.
- **Payments:** Razorpay order created server-side against a DB-verified course/batch fee (or a validated partial amount for installments); payment signature is verified server-side before any student record is created or credited — a cancelled or forged payment never touches the database.
- **Installments:** any number of payments of any amount, online or offline, can be made against a student until their course fee is fully paid. The remaining balance is always computed live from the payment history, never stored/cached.

---

## License

Add your preferred license here (e.g. MIT) — none is currently specified.
