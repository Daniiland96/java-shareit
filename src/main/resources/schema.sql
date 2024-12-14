
create TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL UNIQUE
);

truncate users RESTART IDENTITY cascade;

create TABLE IF NOT EXISTS items (
  id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  available BOOLEAN NOT NULL,
  user_id BIGINT NOT NULL references users(id),
  request_id BIGINT references requests(id)
);

create TABLE IF NOT EXISTS requests (
  id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
  description VARCHAR(512) NOT NULL,
  requestor_id BIGINT references users(id)
);

create TABLE IF NOT EXISTS bookings (
  id BIGINT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id BIGINT NOT NULL references items(id),
  booker_id BIGINT NOT NULL references users(id),
  status VARCHAR(50) NOT NULL
);