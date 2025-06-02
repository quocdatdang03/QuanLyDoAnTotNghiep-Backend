# Graduation Thesis Management System - Backend API (Spring Boot)

## 📌 Overview
This is my personal graduation thesis project built with Spring Boot that provides
RESTful APIs.

This project focuses on developing a system to support registration, management, and progress tracking of graduation thesis projects for students, instructors, and academic staff (admin) at the UTE - The University of Da Nang 

The system enables:
- **🧑🏻‍🎓 Student features:**
  - Submit requests for selecting their desired thesis instructors.
  - Register graduation thesis project.
  - Submit progress reports.
  - Receive timely feedback from their instructors.


- **👩🏻‍🏫 Instructor features:**
    - Monitor, evaluate, and respond to student progress.


- **🧑🏻‍🏫 Department head features:**
    - Assign instructors to students.
    - Aggregate lists of students, thesis project of students, and instructors.


- **🧑🏻‍🏫 Academic staff (ADMIN):**
    - Manage school year and semesters.
    - Manage student accounts.
    - Manage teacher accounts.
    - Manage students in semesters.

## 🚀 Features
- CRUD operations with Spring Data JPA
- Exception Handling (Global handler)
- Pagination and filtering
- Send email using Java Mail Sender
- Authentication and Authorization (Spring Security, JWT)
- Chat realtime using Spring Websocket
- Dockerized deployment

## 🔧 Technologies Used
- **Language**: *Java*
- **Framework**: *Spring Boot*
- **Library**: Spring Data JPA, Spring Security, JWT, Lombok, Spring Websocket, Java Mail Sender
- **Database**: *MySQL*
- **Other tools**: *Docker, Postman*
- **IDE**: IntelliJ IDEA

## 🔗 Frontend Repository

This backend API is used by the Frontend client built with ReactJS.  
👉 Check it out here: [Graduation Thesis Management – Frontend (ReactJS)](https://github.com/quocdatdang03/QuanLyDoAnTotNghiep-Frontend)
