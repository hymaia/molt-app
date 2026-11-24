-- Demand (open missions asking for a skill) vs supply (available talents with it).
with demand as (
    select ms.skill, count(*) as open_missions
    from mission_skills ms
    join missions m on m.id = ms.mission_id
    where m.status = 'OPEN'
    group by ms.skill
),
supply as (
    select ts.skill, count(*) as available_talents
    from talent_skills ts
    join talents t on t.id = ts.talent_id
    where t.available
    group by ts.skill
)
select
    d.skill,
    d.open_missions,
    coalesce(s.available_talents, 0) as available_talents
from demand d
left join supply s using (skill)
order by d.open_missions desc, d.skill
