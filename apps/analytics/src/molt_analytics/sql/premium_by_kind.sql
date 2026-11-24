select
    kind,
    count(*) as talents,
    count(*) filter (where daily_rate_cents > 100000) as premium,
    round(max(daily_rate_cents) / 100, 2) as max_daily_rate_eur
from talents
group by kind
order by kind
