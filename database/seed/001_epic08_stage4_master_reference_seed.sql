-- EPIC-08 Stage 4 - Development/Test Master and Reference Seed Data
--
-- Review artifact only. Do not execute this script yet.
-- It is derived from the current mock repositories and the documented
-- Stage 2 schema. It does not connect to Oracle and does not commit data.
--
-- Classification:
--   CONFIRMED: value exists in current requirements or mock data.
--   PROPOSED: value is copied into a documented proposed entity mapping.
--   TBD: required value or relationship is not established and is omitted.
--
-- Surrogate IDs below are stable development/test seed identifiers only. The
-- production identity-versus-sequence strategy remains TBD in Stage 2.
-- They must not be treated as an approved production key-generation policy.
--
-- No application code, mock repository, JDBC, PL/SQL, or Wicket page is
-- changed by this review script.

-- ---------------------------------------------------------------------------
-- 1. Master/reference data
-- ---------------------------------------------------------------------------

-- CONFIRMED values copied from MockSupplierRepository.
INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NO, SUPPLIER_NAME)
VALUES (1001, '1049', 'YEX BV');

INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NO, SUPPLIER_NAME)
VALUES (1002, '1053', 'TENFOOD BV');

INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NO, SUPPLIER_NAME)
VALUES (1003, '1068', 'PHU IMPORT EXPORT');

INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NO, SUPPLIER_NAME)
VALUES (1004, '1312', 'MEDICAL PLUS GMBH');

INSERT INTO SUPPLIER (SUPPLIER_ID, SUPPLIER_NO, SUPPLIER_NAME)
VALUES (1005, '1399', 'COCA-COLA EURO PACIFIC PARTNERS');

-- PROPOSED mapping of the Stock Assignment merchGroup display value.
-- The code/name meaning remains TBD; no name is invented.
INSERT INTO MERCH_GROUP (MERCH_GROUP_ID, MERCH_GROUP_CODE)
VALUES (2001, '355/5/25');

-- PROPOSED mapping of the Stock Assignment DC detail display values.
-- DC_NAME is copied from the mock display value; whether it is a name, code,
-- or label remains TBD in the schema documentation.
INSERT INTO DISTRIBUTION_CENTER (DISTRIBUTION_CENTER_ID, DC_NO, DC_NAME)
VALUES (3001, '85590', 'MLG MARL');

-- ---------------------------------------------------------------------------
-- 2. Articles from DSD Order and Stock Assignment mock data
-- ---------------------------------------------------------------------------

-- CONFIRMED values copied from MockDsdOrderArticleRepository.
INSERT INTO ARTICLE (
    ARTICLE_ID, ARTICLE_NO, SUBSYSTEM_NO, VARIANT_NO, BUNDLE_NO,
    SG_CNU, DESCRIPTION, SORT_TEXT, SIZE_TEXT
) VALUES (
    4001, '100245', '01', '2', '1',
    '2710', 'Organic bananas 1kg', 'Organic bananas', '1kg'
);

INSERT INTO ARTICLE (
    ARTICLE_ID, ARTICLE_NO, SUBSYSTEM_NO, VARIANT_NO, BUNDLE_NO,
    SG_CNU, DESCRIPTION, SORT_TEXT, SIZE_TEXT
) VALUES (
    4002, '100246', '02', '4', '1',
    '2710', 'Fairtrade bananas 1kg', 'Fairtrade bananas', '1kg'
);

INSERT INTO ARTICLE (
    ARTICLE_ID, ARTICLE_NO, SUBSYSTEM_NO, VARIANT_NO, BUNDLE_NO,
    SG_CNU, DESCRIPTION, SORT_TEXT, SIZE_TEXT
) VALUES (
    4003, '200311', '01', '5', '2',
    '2105', 'Red seedless grapes 500g', 'Red seedless grapes', '500g'
);

INSERT INTO ARTICLE (
    ARTICLE_ID, ARTICLE_NO, SUBSYSTEM_NO, VARIANT_NO, BUNDLE_NO,
    SG_CNU, DESCRIPTION, SORT_TEXT, SIZE_TEXT
) VALUES (
    4004, '300118', '03', '9', '3',
    '1710', 'Avocado Hass loose', 'Avocado Hass', 'loose'
);

INSERT INTO ARTICLE (
    ARTICLE_ID, ARTICLE_NO, SUBSYSTEM_NO, VARIANT_NO, BUNDLE_NO,
    SG_CNU, DESCRIPTION, SORT_TEXT, SIZE_TEXT
) VALUES (
    4005, '400502', '01', '1', '1',
    '1610', 'Fresh strawberries 400g', 'Fresh strawberries', '400g'
);

-- CONFIRMED values copied from MockStockAssignmentRepository.
-- The merch-group display value is mapped to the proposed MERCH_GROUP row.
INSERT INTO ARTICLE (
    ARTICLE_ID, ARTICLE_NO, SUBSYSTEM_NO, VARIANT_NO, BUNDLE_NO,
    SG_CNU, DESCRIPTION, SORT_TEXT, SIZE_TEXT, MERCH_GROUP_ID
) VALUES (
    4006, '379673', '283614', '1', '1',
    '3553831', '10kg ROTKOHL', 'DE', '', 2001
);

INSERT INTO ARTICLE (
    ARTICLE_ID, ARTICLE_NO, SUBSYSTEM_NO, VARIANT_NO, BUNDLE_NO,
    SG_CNU, DESCRIPTION, SORT_TEXT, SIZE_TEXT, MERCH_GROUP_ID
) VALUES (
    4007, '379676', '283616', '1', '2',
    '3553831', 'ROTKOHL', 'DE', '', 2001
);

-- ---------------------------------------------------------------------------
-- 3. DSD order headers and lines
-- ---------------------------------------------------------------------------

-- CONFIRMED values copied from MockDsdOrderArticleRepository.
-- STORE_ID and order status are omitted because no mock value or approved
-- relationship/value exists for them.
INSERT INTO ORDER_HEADER (
    ORDER_HEADER_ID, ORDER_NO, ORDER_LIST_CODE, SUPPLIER_ID
) VALUES (
    5001, '45001234', 'DSD-151-11', 1001
);

INSERT INTO ORDER_HEADER (
    ORDER_HEADER_ID, ORDER_NO, ORDER_LIST_CODE, SUPPLIER_ID
) VALUES (
    5002, '45001258', 'DSD-151-11', 1003
);

INSERT INTO ORDER_HEADER (
    ORDER_HEADER_ID, ORDER_NO, ORDER_LIST_CODE, SUPPLIER_ID
) VALUES (
    5003, '45001302', 'DSD-151-11', 1003
);

INSERT INTO ORDER_HEADER (
    ORDER_HEADER_ID, ORDER_NO, ORDER_LIST_CODE, SUPPLIER_ID
) VALUES (
    5004, '45001344', 'DSD-151-12', 1004
);

-- The mock has two rows for order 45001234 with the same supplier and order
-- list, so they are represented as two lines under one header.
INSERT INTO ORDER_LINE (
    ORDER_LINE_ID, ORDER_HEADER_ID, ARTICLE_ID, SUBSYSTEM_NO,
    VARIANT_NO, BUNDLE_NO, ORDER_QUANTITY, PRICE, MRP, PROMOTION
) VALUES (
    6001, 5001, 4001, '01', '2', '1', 0.00, 12.50, 0.00, '0.00'
);

INSERT INTO ORDER_LINE (
    ORDER_LINE_ID, ORDER_HEADER_ID, ARTICLE_ID, SUBSYSTEM_NO,
    VARIANT_NO, BUNDLE_NO, ORDER_QUANTITY, PRICE, MRP, PROMOTION
) VALUES (
    6002, 5001, 4002, '02', '4', '1', 0.00, 13.00, 0.00, '0.00'
);

INSERT INTO ORDER_LINE (
    ORDER_LINE_ID, ORDER_HEADER_ID, ARTICLE_ID, SUBSYSTEM_NO,
    VARIANT_NO, BUNDLE_NO, ORDER_QUANTITY, PRICE, MRP, PROMOTION
) VALUES (
    6003, 5002, 4003, '01', '5', '2', 0.00, 18.50, 0.00, '0.00'
);

INSERT INTO ORDER_LINE (
    ORDER_LINE_ID, ORDER_HEADER_ID, ARTICLE_ID, SUBSYSTEM_NO,
    VARIANT_NO, BUNDLE_NO, ORDER_QUANTITY, PRICE, MRP, PROMOTION
) VALUES (
    6004, 5003, 4004, '03', '9', '3', 0.00, 22.00, 0.00, '0.00'
);

INSERT INTO ORDER_LINE (
    ORDER_LINE_ID, ORDER_HEADER_ID, ARTICLE_ID, SUBSYSTEM_NO,
    VARIANT_NO, BUNDLE_NO, ORDER_QUANTITY, PRICE, MRP, PROMOTION
) VALUES (
    6005, 5004, 4005, '01', '1', '1', 0.00, 15.00, 0.00, '0.00'
);

-- ---------------------------------------------------------------------------
-- 4. Stock Assignment development/test rows
-- ---------------------------------------------------------------------------

-- CONFIRMED values copied from MockStockAssignmentRepository.
-- No DC_ID or STORE_ID is assigned: the result mock has no store value and
-- the separate DC detail row has no article identity to establish a safe
-- STOCK_ASSIGNMENT-to-DC_STOCK relationship.
INSERT INTO STOCK_ASSIGNMENT (
    STOCK_ASSIGNMENT_ID, ARTICLE_ID, SALES_FORECAST, STORE_QUANTITY,
    ASSIGNED_STOCK_QUANTITY, QUANTITY_GAP, REMAINING_QUANTITY, UFM_STOCK,
    RECALC, KEY_DISTRIBUTION, IBC, ARTICLE_EXCHANGE, PROMOTION
) VALUES (
    7001, 4006, 205.00, 0.00, 0.00, 0.00, 0.00, 100.00,
    NULL, NULL, NULL, NULL, NULL
);

INSERT INTO STOCK_ASSIGNMENT (
    STOCK_ASSIGNMENT_ID, ARTICLE_ID, SALES_FORECAST, STORE_QUANTITY,
    ASSIGNED_STOCK_QUANTITY, QUANTITY_GAP, REMAINING_QUANTITY, UFM_STOCK,
    RECALC, KEY_DISTRIBUTION, IBC, ARTICLE_EXCHANGE, PROMOTION
) VALUES (
    7002, 4007, 998.00, 0.00, 0.00, 2.00, 2.00, 75.00,
    NULL, NULL, NULL, NULL, NULL
);

-- ---------------------------------------------------------------------------
-- 5. Intentionally omitted/TBD seed data
-- ---------------------------------------------------------------------------
-- APP_USER: configured credentials are not a persisted user model and the
-- password hashing contract is TBD.
-- STORE: no current Store model or store values exist.
-- SUPPLIER_ASSIGNMENT: no implemented assignment behavior or effective-date
-- relationship is established by the current models.
-- STORE_STOCK: no store identity or approved stock snapshot semantics exist.
-- DC_STOCK: the mock DC detail row lacks article identity.
-- STEERING_PARAMETER: EPIC-07 is postponed and no model exists.
-- AUDIT_LOG: no current audit behavior, event values, or retention policy.
-- DSD cbb/moq/stock values: no corresponding documented physical columns.
-- Stock Assignment DC detail order factor/average NNBP: no documented target
-- columns or safe article relationship exists.
-- No COMMIT is included because transaction ownership remains TBD.

-- End of review-only Stage 4 seed data.
