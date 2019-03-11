(
select inv.bid, inv.cd_bib from tbc_inventario inv
where not inv.fl_canc='S'
and inv.cd_sit ='2'
group by 1,2
UNION
select tt1.bid_coll, inv1.cd_bib from tbc_inventario inv1
inner join tr_tit_tit tt1 on inv1.bid=tt1.bid_base
where tt1.cd_natura_coll in ('M','C','S') 
and not tt1.fl_canc='S'
and not inv1.fl_canc='S'
and inv1.cd_sit='2'
group by 1,2
UNION
select tt2.bid_coll, inv1.cd_bib from tbc_inventario inv1
inner join tr_tit_tit tt1 on inv1.bid=tt1.bid_base
inner join tr_tit_tit tt2 on tt1.bid_coll=tt2.bid_base
where inv1.cd_sit='2'
and not inv1.fl_canc='S'
and tt1.cd_natura_coll in ('M','C','S') 
and not tt1.fl_canc='S'
and tt2.cd_natura_coll in ('M','C','S') 
and not tt2.fl_canc='S'
group by 1,2
UNION
select tt3.bid_coll, inv1.cd_bib from tbc_inventario inv1
inner join tr_tit_tit tt1 on inv1.bid=tt1.bid_base
inner join tr_tit_tit tt2 on tt1.bid_coll=tt2.bid_base
inner join tr_tit_tit tt3 on tt2.bid_coll=tt3.bid_base
where inv1.cd_sit='2'
and not inv1.fl_canc='S'
and tt1.cd_natura_coll in ('M','C','S') 
and not tt1.fl_canc='S'
and tt2.cd_natura_coll in ('M','C','S') 
and not tt2.fl_canc='S'
and tt3.cd_natura_coll in ('M','C','S') 
and not tt3.fl_canc='S'
group by 1,2
UNION
select ttn.bid_base,inv2.cd_bib from tr_tit_tit ttn
inner join tbc_inventario inv2 on inv2.bid =ttn.bid_coll
where ttn.cd_natura_base = 'N'
  and ttn.cd_natura_coll in ('M','S','W')
  and not ttn.fl_canc='S'
  group by 1,2  
UNION
select ttb.bid,ttb.cd_biblioteca from tr_tit_bib ttb 
  where ttb.fl_canc <> 'S'
 and ttb.uri_copia > ' ' 
  group by 1,2
  );
  

  