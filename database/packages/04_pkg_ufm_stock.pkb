CREATE OR REPLACE PACKAGE PKG_UFM_STOCK AS

    PROCEDURE GET_STORE_STOCK(
        P_STORE_NO IN STORE.STORE_NO%TYPE,
        P_ARTICLE_NO IN ARTICLE.ARTICLE_NO%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

    PROCEDURE GET_DC_STOCK(
        P_DC_NO IN DISTRIBUTION_CENTER.DC_NO%TYPE,
        P_ARTICLE_NO IN ARTICLE.ARTICLE_NO%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    );

END PKG_UFM_STOCK;
/

CREATE OR REPLACE PACKAGE BODY PKG_UFM_STOCK AS

    PROCEDURE GET_STORE_STOCK(
        P_STORE_NO IN STORE.STORE_NO%TYPE,
        P_ARTICLE_NO IN ARTICLE.ARTICLE_NO%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    )
    AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT
                SS.STORE_STOCK_ID,
                ST.STORE_NO,
                ST.STORE_NAME,
                A.ARTICLE_NO,
                A.DESCRIPTION,
                SS.QUANTITY,
                SS.AS_OF_AT
            FROM STORE_STOCK SS
            JOIN STORE ST
                ON ST.STORE_ID = SS.STORE_ID
            JOIN ARTICLE A
                ON A.ARTICLE_ID = SS.ARTICLE_ID
            WHERE
                (P_STORE_NO IS NULL
                    OR ST.STORE_NO = P_STORE_NO)
                AND
                (P_ARTICLE_NO IS NULL
                    OR A.ARTICLE_NO = P_ARTICLE_NO)
            ORDER BY ST.STORE_NO, A.ARTICLE_NO;
    END GET_STORE_STOCK;


    PROCEDURE GET_DC_STOCK(
        P_DC_NO IN DISTRIBUTION_CENTER.DC_NO%TYPE,
        P_ARTICLE_NO IN ARTICLE.ARTICLE_NO%TYPE,
        P_RESULT OUT SYS_REFCURSOR
    )
    AS
    BEGIN
        OPEN P_RESULT FOR
            SELECT
                DS.DC_STOCK_ID,
                DC.DC_NO,
                DC.DC_NAME,
                A.ARTICLE_NO,
                A.DESCRIPTION,
                DS.QUANTITY,
                DS.AS_OF_AT
            FROM DC_STOCK DS
            JOIN DISTRIBUTION_CENTER DC
                ON DC.DISTRIBUTION_CENTER_ID =
                   DS.DISTRIBUTION_CENTER_ID
            JOIN ARTICLE A
                ON A.ARTICLE_ID = DS.ARTICLE_ID
            WHERE
                (P_DC_NO IS NULL
                    OR DC.DC_NO = P_DC_NO)
                AND
                (P_ARTICLE_NO IS NULL
                    OR A.ARTICLE_NO = P_ARTICLE_NO)
            ORDER BY DC.DC_NO, A.ARTICLE_NO;
    END GET_DC_STOCK;

END PKG_UFM_STOCK;
/