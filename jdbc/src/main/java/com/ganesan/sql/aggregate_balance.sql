SELECT  REGEXP_SUBSTR(a.ACCT_NO,'[a-z0-9._%+]+-')  FROM ACCOUNTS a  ORDER BY a.UPDATED_AT DESC 


SELECT
	*
FROM
	(
	SELECT
		count(1) AS cnt,
		b.acct_init,
		sum (b.balance) AS balsum,
		min(b.balance) AS balmin,
		max(b.balance) AS balmax
	FROM
		(
		SELECT
			REGEXP_SUBSTR(a.ACCT_NO, '[a-z0-9._%+]+-') AS acct_init,
			a.*
		FROM
			ACCOUNTS a ) b
	GROUP BY
		b.acct_init
	ORDER BY
		b.acct_init DESC ) c
ORDER BY
	c.balsum DESC



-- SELECT count(1) cnt, a.ACCT_NO FROM ACCOUNTS a GROUP BY REGEXP_SUBSTR(a.ACCT_NO,'[a-z0-9._%+-]+-')  ORDER BY cnt DESC 