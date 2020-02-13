-- Project Name : learning-support
-- Date/Time    : 2016/07/13 14:10:52
-- Author       : Masayuki IGA
-- RDBMS Type   : PostgreSQL
-- Application  : A5:SQL Mk-2

-- learning-supportというDBを新規に作成し、以下のSQL文を実行してください。

-- 管理者
drop table if exists admins cascade;

create table admins (
  id serial
  , name varchar(50) not null
  , kana varchar(50) not null
  , email varchar(50) not null unique
  , password varchar(100) not null
  , can_show_all_company boolean
  , constraint admins_PKC primary key (id)
) ;

-- 企業担当者
drop table if exists company_members cascade;

create table company_members (
  id serial
  , name varchar(50) not null
  , kana varchar(50) not null
  , email varchar(50) not null unique
  , password varchar(100) not null
  , company_id integer not null
  , constraint company_members_PKC primary key (id)
) ;

-- 企業
drop table if exists companies cascade;

create table companies (
  id serial
  , name varchar(50) not null
  , kana varchar(100) not null
  , remarks text
  , constraint companies_PKC primary key (id)
) ;

-- 日報
drop table if exists daily_reports cascade;

create table daily_reports (
  id serial
  , date date not null
  , training_id integer not null
  , student_id integer not null
  , content text
  , intelligibility integer
  , detail_intelligibillity text
  , about_instructor integer
  , question text
  , constraint daily_reports_PKC primary key (id)
) ;

-- 受講生　所感
drop table if exists student_impressions cascade;

create table student_impressions (
  id serial
  , weekly_report_id integer
  , student_name varchar(50)
  , content text
) ;

-- 週報
drop table if exists weekly_reports cascade;

create table weekly_reports (
  id serial
  , start_date date not null
  , instructor_name varchar(50) not null
  , content text
  , constraint weekly_reports_PKC primary key (id)
  , training_id integer not null
) ;

-- 研修スケジュール
drop table if exists training_student cascade;

create table training_student (
  training_id integer not null
  , student_id integer not null
) ;

-- 受講生
drop table if exists students cascade;

create table students (
  id serial
  , name varchar(50) not null
  , kana varchar(50) not null
  , email varchar(50) not null unique
  , password varchar(100) not null
  , company_id integer not null
  , constraint students_PKC primary key (id)
) ;


-- 研修
drop table if exists trainings cascade;

create table trainings (
  id serial
  , start_date date not null
  , end_date date not null
  , name Varchar(200) not null
  , instructor_id integer not null
  , sub_instructor_id1 integer
  , sub_instructor_id2 integer
  , sub_instructor_id3 integer
  , constraint trainings_PKC primary key (id)
) ;

-- 講師
drop table if exists instructors cascade;

create table instructors (
  id serial
  , name varchar(50) not null
  , kana varchar(50) not null
  , email varchar(50) not null unique
  , password varchar(100) not null
  , affiliation varchar(50) not null
  , remarks text
  , constraint instructors_PKC primary key (id)
) ;

-- 管理者と企業の中間テーブル
Drop table if exists admin_company cascade;

Create table admin_company (
  admin_id integer not null
 ,company_id integer not null
);