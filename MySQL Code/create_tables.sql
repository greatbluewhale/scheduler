create table users (
    user_id varchar(32) not null,
    password varchar(32) not null,
    avail_start time,
    avail_end time,
    constraint pk_users primary key (user_id)
);

create table events (
    event_id integer not null,
    title varchar(32) not null,
    location varchar(32),
    start_date date not null,
    start_time time not null,
    end_time time not null,
    recurrence smallint not null,
    stop_date date,
    constraint pk_events primary key (event_id)
);

create table users_events (
    user_id varchar(32) not null,
    event_id integer not null,
    constraint pk_users_events primary key (user_id, event_id),
    constraint fk_user_id foreign key (user_id) references users (user_id),
    constraint fk_event_id foreign key (event_id) references events (event_id)
);

insert into users (user_id, password, avail_start, avail_end)
    values ('nickdyszel', 'password', '9:00', '20:00');
insert into users (user_id, password, avail_start, avail_end)
    values ('friend', 'friend', '8:00', '18:00');

insert into events (event_id, title, location, start_date, start_time, end_time, recurrence)
    values (0, 'Project Demo', '220 IST', '2012-12-13', '13:00', '14:15', 0);
insert into events (event_id, title, start_date, start_time, end_time, recurrence, stop_date)
    values (1, 'Monthly dinner', '2012-12-1', '18:30', '20:30', 4, '2013-12-31');
insert into events (event_id, title, start_date, start_time, end_time, recurrence, stop_date)
    values (2, 'Birthday Party!', '2010-1-3', '15:30', '18:30', 5, '2015-1-3');

insert into users_events (user_id, event_id) values ('nickdyszel', 0);
insert into users_events (user_id, event_id) values ('nickdyszel', 1);
insert into users_events (user_id, event_id) values ('nickdyszel', 2);
insert into users_events (user_id, event_id) values ('friend', 1);