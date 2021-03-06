SELECT acc.*
FROM account acc
         INNER JOIN
     (SELECT source_id, SUM(amount) AS transfer_amount
      FROM transfer
      WHERE transfer_time > '2019-01-01'
      GROUP BY source_id) t ON t.source_id = acc.id
WHERE t.transfer_amount > 1000;
