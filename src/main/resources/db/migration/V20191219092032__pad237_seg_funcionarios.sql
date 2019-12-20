CREATE TABLE seg_funcionarios
(
    id BIGINT   IDENTITY(1, 1)  NOT NULL PRIMARY KEY,
    nome        VARCHAR(50)     NOT NULL,
    email       VARCHAR(50)     NOT NULL,
    uuid        VARCHAR(36)     NOT NULL
);

CREATE UNIQUE index ix_seg_funcionarios_01 ON seg_funcionarios (email asc);