-- Mission funnel: posted -> contracted -> done.
with stages as (
    select 1 as step, 'posted' as stage, count(*) as missions from missions
    union all
    select 2, 'contracted', count(*) from missions where status in ('CONTRACTED', 'DONE')
    union all
    select 3, 'done', count(*) from missions where status = 'DONE'
)
select
    stage,
    missions,
    round(100.0 * missions / first_value(missions) over (order by step), 1) as conversion_pct
from stages
order by step
