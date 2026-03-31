-- This file allow to write SQL commands that will be emitted in test and dev.
-- =============================================================
--  DDL — PostgreSQL
--  Gerado a partir do MER Completo v1
--  Schema: public
-- =============================================================

-- Extensão para geração de UUID
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =============================================================
--  EMPRESA
-- =============================================================

CREATE TABLE empresas (
                          id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
                          razao_social        VARCHAR(200)    NOT NULL,
                          cnpj                CHAR(18)        NOT NULL,
                          inscricao_estadual  VARCHAR(20),
                          ramo_atividade      VARCHAR(100),
                          status              VARCHAR(20)     NOT NULL DEFAULT 'ATIVO',
                          logradouro          VARCHAR(200),
                          numero              VARCHAR(20),
                          complemento         VARCHAR(100),
                          bairro              VARCHAR(100),
                          cidade              VARCHAR(100),
                          uf                  CHAR(2),
                          cep                 CHAR(9),
                          data_cadastro       TIMESTAMP       NOT NULL DEFAULT NOW(),
                          created_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
                          updated_at          TIMESTAMP       NOT NULL DEFAULT NOW(),
                          deleted_at          TIMESTAMP,

                          CONSTRAINT pk_empresas      PRIMARY KEY (id),
                          CONSTRAINT uq_empresas_cnpj UNIQUE (cnpj)
);

COMMENT ON TABLE  empresas                  IS 'Prestadores de serviço vinculados à Move';
COMMENT ON COLUMN empresas.status           IS 'ATIVO | INATIVO';
COMMENT ON COLUMN empresas.deleted_at       IS 'Preenchido indica exclusão lógica';

-- =============================================================
--  PESSOA
-- =============================================================

CREATE TABLE pessoas (
                         id                      UUID            NOT NULL DEFAULT gen_random_uuid(),
                         nome_completo           VARCHAR(200)    NOT NULL,
                         nome_social             VARCHAR(200),
                         sexo                    VARCHAR(20),
                         data_nascimento         DATE,
                         nome_mae                VARCHAR(200),
                         nome_pai                VARCHAR(200),
                         raca                    VARCHAR(30),
                         religiao                VARCHAR(50),
                         nacionalidade           VARCHAR(60),
                         naturalidade            VARCHAR(100),
                         tipo_sanguineo          VARCHAR(5),
                         estado_civil            VARCHAR(30),
                         possui_dependentes      BOOLEAN         NOT NULL DEFAULT FALSE,
                         pcd                     BOOLEAN         NOT NULL DEFAULT FALSE,
                         tipo_deficiencia        VARCHAR(60),
                         escolaridade            VARCHAR(60),
                         instituicao_escolar     VARCHAR(200),
                         data_conclusao_escolar  DATE,
                         email                   VARCHAR(200),
                         logradouro              VARCHAR(200),
                         numero                  VARCHAR(20),
                         complemento             VARCHAR(100),
                         bairro                  VARCHAR(100),
                         cidade                  VARCHAR(100),
                         uf                      CHAR(2),
                         cep                     CHAR(9),
                         data_cadastro           TIMESTAMP       NOT NULL DEFAULT NOW(),
                         created_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
                         updated_at              TIMESTAMP       NOT NULL DEFAULT NOW(),
                         deleted_at              TIMESTAMP,

                         CONSTRAINT pk_pessoas PRIMARY KEY (id)
);

COMMENT ON TABLE  pessoas            IS 'Cadastro central de pessoas físicas';
COMMENT ON COLUMN pessoas.deleted_at IS 'Preenchido indica exclusão lógica';

-- =============================================================
--  DOCUMENTOS DA PESSOA
--  CTPS e CNH absorvidos aqui (relação 1:1 com pessoas)
-- =============================================================

CREATE TABLE pessoa_documentos (
                                   id                  UUID        NOT NULL DEFAULT gen_random_uuid(),
                                   pessoa_id           UUID        NOT NULL,
                                   cpf                 CHAR(14)    NOT NULL,
                                   rg                  VARCHAR(20),
                                   data_expedicao_rg   DATE,
                                   orgao_emissor_rg    VARCHAR(10),
                                   pis_pasep           VARCHAR(20),
                                   nis                 VARCHAR(20),
                                   cns                 VARCHAR(20),
                                   titulo_eleitor      VARCHAR(20),
                                   titulo_eleitor_zona VARCHAR(10),
                                   titulo_eleitor_secao VARCHAR(10),
                                   reservista          VARCHAR(20),
    -- CTPS
                                   numero_ctps         VARCHAR(20),
                                   serie_ctps          VARCHAR(10),
                                   data_expedicao_ctps DATE,
                                   uf_ctps             CHAR(2),
    -- CNH
                                   numero_cnh          VARCHAR(20),
                                   categoria_cnh       VARCHAR(5),
                                   validade_cnh        DATE,

                                   CONSTRAINT pk_pessoa_documentos         PRIMARY KEY (id),
                                   CONSTRAINT uq_pessoa_documentos_cpf     UNIQUE (cpf),
                                   CONSTRAINT uq_pessoa_documentos_pessoa  UNIQUE (pessoa_id),
                                   CONSTRAINT fk_pessoa_documentos_pessoa  FOREIGN KEY (pessoa_id)
                                       REFERENCES pessoas (id)
);

COMMENT ON TABLE pessoa_documentos IS 'Documentos da pessoa: RG, CPF, PIS, CTPS, CNH, etc.';

-- =============================================================
--  TELEFONES
--  Pode pertencer a pessoa ou empresa (FKs opcionais)
-- =============================================================

CREATE TABLE telefones (
                           id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                           pessoa_id   UUID,
                           empresa_id  UUID,
                           numero      VARCHAR(20) NOT NULL,
                           tipo        VARCHAR(20),

                           CONSTRAINT pk_telefones             PRIMARY KEY (id),
                           CONSTRAINT fk_telefones_pessoa      FOREIGN KEY (pessoa_id)
                               REFERENCES pessoas (id),
                           CONSTRAINT fk_telefones_empresa     FOREIGN KEY (empresa_id)
                               REFERENCES empresas (id),
                           CONSTRAINT ck_telefones_vinculo     CHECK (
                               (pessoa_id IS NOT NULL AND empresa_id IS NULL) OR
                               (pessoa_id IS NULL AND empresa_id IS NOT NULL)
                               )
);

COMMENT ON TABLE  telefones             IS 'Telefones de pessoas ou empresas';
COMMENT ON COLUMN telefones.tipo        IS 'CELULAR | FIXO | COMERCIAL | WHATSAPP';
COMMENT ON CONSTRAINT ck_telefones_vinculo ON telefones IS 'Telefone deve pertencer a pessoa OU empresa, nunca ambos';

-- =============================================================
--  CONTATOS DE EMERGÊNCIA
-- =============================================================

CREATE TABLE contatos_emergencia (
                                     id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                                     pessoa_id   UUID        NOT NULL,
                                     nome        VARCHAR(200) NOT NULL,
                                     parentesco  VARCHAR(30),
                                     telefone    VARCHAR(20) NOT NULL,

                                     CONSTRAINT pk_contatos_emergencia           PRIMARY KEY (id),
                                     CONSTRAINT fk_contatos_emergencia_pessoa    FOREIGN KEY (pessoa_id)
                                         REFERENCES pessoas (id)
);

-- =============================================================
--  COLABORADORES
--  empresa_id é referência de filtro da atuação atual
-- =============================================================

CREATE TABLE colaboradores (
                               id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                               pessoa_id   UUID        NOT NULL,
                               empresa_id  UUID        NOT NULL,
                               cargo       VARCHAR(50),
                               responsavel BOOLEAN     NOT NULL DEFAULT FALSE,
                               data_inicio DATE        NOT NULL,
                               created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
                               updated_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
                               deleted_at  TIMESTAMP,

                               CONSTRAINT pk_colaboradores             PRIMARY KEY (id),
                               CONSTRAINT fk_colaboradores_pessoa      FOREIGN KEY (pessoa_id)
                                   REFERENCES pessoas (id),
                               CONSTRAINT fk_colaboradores_empresa     FOREIGN KEY (empresa_id)
                                   REFERENCES empresas (id)
);

COMMENT ON TABLE  colaboradores              IS 'Vínculo ativo entre pessoa e empresa prestadora';
COMMENT ON COLUMN colaboradores.responsavel  IS 'Indica se o colaborador é responsável pela empresa';
COMMENT ON COLUMN colaboradores.deleted_at   IS 'Preenchido indica inativação do vínculo';

-- =============================================================
--  RODÍZIO
-- =============================================================

CREATE TABLE veiculo_rodizio (
                                 id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                                 final_placa SMALLINT    NOT NULL,
                                 descricao   VARCHAR(20) NOT NULL,
                                 status      BOOLEAN     NOT NULL DEFAULT TRUE,

                                 CONSTRAINT pk_veiculo_rodizio               PRIMARY KEY (id),
                                 CONSTRAINT uq_veiculo_rodizio_final_placa   UNIQUE (final_placa),
                                 CONSTRAINT ck_veiculo_rodizio_final_placa   CHECK (final_placa BETWEEN 0 AND 9)
);

COMMENT ON TABLE  veiculo_rodizio           IS 'Regras de rodízio municipal por final de placa';
COMMENT ON COLUMN veiculo_rodizio.descricao IS 'Dia da semana: SEGUNDA-FEIRA, TERÇA-FEIRA, etc.';

-- =============================================================
--  VEÍCULOS
-- =============================================================

CREATE TABLE veiculos (
                          id              UUID        NOT NULL DEFAULT gen_random_uuid(),
                          placa           VARCHAR(10) NOT NULL,
                          renavam         VARCHAR(20) NOT NULL,
                          chassi          VARCHAR(50) NOT NULL,
                          descricao       VARCHAR(100),
                          ano_fabricacao  SMALLINT,
                          ano_modelo      SMALLINT,
                          cor             VARCHAR(30),
                          combustivel     VARCHAR(30),
                          proprietario    VARCHAR(100),
                          cpf_cnpj        VARCHAR(18),
                          situacao        VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL',
                          status          BOOLEAN     NOT NULL DEFAULT TRUE,
                          empresa_id      UUID        NOT NULL,
                          rodizio_id      UUID,
                          created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
                          updated_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
                          deleted_at      TIMESTAMP,

                          CONSTRAINT pk_veiculos              PRIMARY KEY (id),
                          CONSTRAINT uq_veiculos_placa        UNIQUE (placa),
                          CONSTRAINT uq_veiculos_renavam      UNIQUE (renavam),
                          CONSTRAINT uq_veiculos_chassi       UNIQUE (chassi),
                          CONSTRAINT fk_veiculos_empresa      FOREIGN KEY (empresa_id)
                              REFERENCES empresas (id),
                          CONSTRAINT fk_veiculos_rodizio      FOREIGN KEY (rodizio_id)
                              REFERENCES veiculo_rodizio (id),
                          CONSTRAINT ck_veiculos_situacao     CHECK (situacao IN ('DISPONIVEL', 'EM_USO', 'EM_MANUTENCAO', 'RESERVADO'))
);

COMMENT ON TABLE  veiculos            IS 'Frota de veículos da Move';
COMMENT ON COLUMN veiculos.situacao   IS 'DISPONIVEL | EM_USO | EM_MANUTENCAO | RESERVADO';
COMMENT ON COLUMN veiculos.status     IS 'TRUE = ativo no cadastro';
COMMENT ON COLUMN veiculos.deleted_at IS 'Preenchido indica exclusão lógica';

-- =============================================================
--  LOCAIS
-- =============================================================

CREATE TABLE locais (
                        id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                        descricao   VARCHAR(60) NOT NULL,
                        uf          CHAR(2),
                        status      BOOLEAN     NOT NULL DEFAULT TRUE,

                        CONSTRAINT pk_locais PRIMARY KEY (id)
);

COMMENT ON TABLE locais IS 'Pontos de origem e destino nos registros de uso do veículo';

-- =============================================================
--  USO DO VEÍCULO
-- =============================================================

CREATE TABLE veiculo_uso (
                             id              UUID        NOT NULL DEFAULT gen_random_uuid(),
                             veiculo_id      UUID        NOT NULL,
                             colaborador_id  UUID        NOT NULL,
                             km_inicial      INTEGER,
                             km_final        INTEGER,
                             origem_id       UUID        NOT NULL,
                             destino_id      UUID        NOT NULL,
                             dt_saida        TIMESTAMP   NOT NULL,
                             dt_chegada      TIMESTAMP,
                             observacoes     VARCHAR(200),
                             situacao        VARCHAR(20) NOT NULL DEFAULT 'EM_USO',
                             created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
                             updated_at      TIMESTAMP   NOT NULL DEFAULT NOW(),

                             CONSTRAINT pk_veiculo_uso               PRIMARY KEY (id),
                             CONSTRAINT fk_veiculo_uso_veiculo       FOREIGN KEY (veiculo_id)
                                 REFERENCES veiculos (id),
                             CONSTRAINT fk_veiculo_uso_colaborador   FOREIGN KEY (colaborador_id)
                                 REFERENCES colaboradores (id),
                             CONSTRAINT fk_veiculo_uso_origem        FOREIGN KEY (origem_id)
                                 REFERENCES locais (id),
                             CONSTRAINT fk_veiculo_uso_destino       FOREIGN KEY (destino_id)
                                 REFERENCES locais (id),
                             CONSTRAINT ck_veiculo_uso_situacao      CHECK (situacao IN ('EM_USO', 'CONCLUIDO', 'CANCELADO')),
                             CONSTRAINT ck_veiculo_uso_km            CHECK (km_final IS NULL OR km_final >= km_inicial)
);

COMMENT ON TABLE  veiculo_uso           IS 'Registro de uso dos veículos por colaborador';
COMMENT ON COLUMN veiculo_uso.situacao  IS 'EM_USO | CONCLUIDO | CANCELADO';

-- =============================================================
--  ÍNDICES
-- =============================================================

-- Exclusão lógica (queries mais frequentes)
CREATE INDEX idx_empresas_deleted_at        ON empresas       (deleted_at) WHERE deleted_at IS NULL;
CREATE INDEX idx_pessoas_deleted_at         ON pessoas        (deleted_at) WHERE deleted_at IS NULL;
CREATE INDEX idx_colaboradores_deleted_at   ON colaboradores  (deleted_at) WHERE deleted_at IS NULL;
CREATE INDEX idx_veiculos_deleted_at        ON veiculos       (deleted_at) WHERE deleted_at IS NULL;
CREATE INDEX idx_veiculos_empresa_id        ON veiculos       (empresa_id);

-- Lookups frequentes
CREATE INDEX idx_colaboradores_pessoa_id    ON colaboradores  (pessoa_id);
CREATE INDEX idx_colaboradores_empresa_id   ON colaboradores  (empresa_id);
CREATE INDEX idx_veiculo_uso_veiculo_id     ON veiculo_uso    (veiculo_id);
CREATE INDEX idx_veiculo_uso_colaborador_id ON veiculo_uso    (colaborador_id);
CREATE INDEX idx_veiculo_uso_dt_saida       ON veiculo_uso    (dt_saida);
CREATE INDEX idx_veiculo_uso_situacao       ON veiculo_uso    (situacao);
CREATE INDEX idx_telefones_pessoa_id        ON telefones      (pessoa_id) WHERE pessoa_id IS NOT NULL;
CREATE INDEX idx_telefones_empresa_id       ON telefones      (empresa_id) WHERE empresa_id IS NOT NULL;