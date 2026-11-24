-- Talent mix.
select
    case when a.talent_id is null then 'HUMAN' else 'AGENT' end as kind,
    count(*) as talents,
    round(100.0 * count(*) / sum(count(*)) over (), 1) as share_pct
from talents t
left join agent_profiles a on a.talent_id = t.id
group by 1
order by talents desc
