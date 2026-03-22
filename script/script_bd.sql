create database if not exists bank_consultant;
use bank_consultant;

create table users (
    id bigint primary key auto_increment,
    full_name varchar(255) not null,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    phone varchar(20),
    birth_date date,
    role enum('user', 'admin') not null default 'user',
    is_active boolean not null default true,
    created_at timestamp not null default current_timestamp
);

create table banks (
    id bigint primary key auto_increment,
    name varchar(255) not null,
    logo_url varchar(500),
    description text,
    min_income decimal(12,2) not null default 0,
    max_amount decimal(12,2) not null,
    min_term int not null,
    max_term int not null,
    base_rate decimal(5,2) not null,
    created_at timestamp not null default current_timestamp
);

create table bank_credit_types (
    id bigint primary key auto_increment,
    bank_id bigint not null,
    credit_type varchar(50) not null,
    foreign key (bank_id) references banks(id) on delete cascade
);

create table credit_offers (
    id bigint primary key auto_increment,
    bank_id bigint not null,
    credit_type varchar(50) not null,
    min_rate decimal(5,2) not null,
    max_rate decimal(5,2) not null,
    required_documents json,
    foreign key (bank_id) references banks(id) on delete cascade
);

create table bank_offices (
    id bigint primary key auto_increment,
    bank_id bigint not null,
    address varchar(500) not null,
    lat decimal(10,8) not null,
    lng decimal(11,8) not null,
    work_time varchar(255),
    phone varchar(20),
    foreign key (bank_id) references banks(id) on delete cascade
);

create table loan_requests (
    id bigint primary key auto_increment,
    user_id bigint not null,
    amount decimal(12,2) not null,
    term_months int not null,
    purpose varchar(255),
    income decimal(12,2) not null,
    employment_type varchar(50),
    credit_history varchar(50),
    monthly_payment decimal(12,2),
    selected_bank_id bigint,
    status enum('in_progress', 'bank_selected', 'completed') not null default 'in_progress',
    created_at timestamp not null default current_timestamp,
    foreign key (user_id) references users(id) on delete cascade,
    foreign key (selected_bank_id) references banks(id) on delete set null
);