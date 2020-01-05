CREATE TABLE min_1
(
       "symbol" VARCHAR(255) NOT NULL,
       "timestamp" TIMESTAMP NOT NULL,
       "open" DOUBLE PRECISION NOT NULL,
       "high" DOUBLE PRECISION NOT NULL,
       "low" DOUBLE PRECISION NOT NULL,
       "close" DOUBLE PRECISION NOT NULL,
       "volume" BIGINT NOT NULL,
       PRIMARY KEY("symbol", "timestamp")
);

CREATE TABLE min_5
(
       "symbol" VARCHAR(255) NOT NULL,
       "timestamp" TIMESTAMP NOT NULL,
       "open" DOUBLE PRECISION NOT NULL,
       "high" DOUBLE PRECISION NOT NULL,
       "low" DOUBLE PRECISION NOT NULL,
       "close" DOUBLE PRECISION NOT NULL,
       "volume" BIGINT NOT NULL,
       PRIMARY KEY("symbol", "timestamp")
);

CREATE TABLE min_15
(
       "symbol" VARCHAR(255) NOT NULL,
       "timestamp" TIMESTAMP NOT NULL,
       "open" DOUBLE PRECISION NOT NULL,
       "high" DOUBLE PRECISION NOT NULL,
       "low" DOUBLE PRECISION NOT NULL,
       "close" DOUBLE PRECISION NOT NULL,
       "volume" BIGINT NOT NULL,
       PRIMARY KEY("symbol", "timestamp")
);

CREATE TABLE min_30
(
       "symbol" VARCHAR(255) NOT NULL,
       "timestamp" TIMESTAMP NOT NULL,
       "open" DOUBLE PRECISION NOT NULL,
       "high" DOUBLE PRECISION NOT NULL,
       "low" DOUBLE PRECISION NOT NULL,
       "close" DOUBLE PRECISION NOT NULL,
       "volume" BIGINT NOT NULL,
       PRIMARY KEY("symbol", "timestamp")
);

CREATE TABLE hour_1
(
       "symbol" VARCHAR(255) NOT NULL,
       "timestamp" TIMESTAMP NOT NULL,
       "open" DOUBLE PRECISION NOT NULL,
       "high" DOUBLE PRECISION NOT NULL,
       "low" DOUBLE PRECISION NOT NULL,
       "close" DOUBLE PRECISION NOT NULL,
       "volume" BIGINT NOT NULL,
       PRIMARY KEY("symbol", "timestamp")
);

CREATE TABLE day_1
(
       "symbol" VARCHAR(255) NOT NULL,
       "timestamp" TIMESTAMP NOT NULL,
       "open" DOUBLE PRECISION NOT NULL,
       "high" DOUBLE PRECISION NOT NULL,
       "low" DOUBLE PRECISION NOT NULL,
       "close" DOUBLE PRECISION NOT NULL,
       "volume" BIGINT NOT NULL,
       PRIMARY KEY("symbol", "timestamp")
);