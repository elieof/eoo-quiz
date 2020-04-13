var status = rs.status();
if (status.errmsg === 'no replset config has been received') {
    rs.initiate();
}
for (var i = 1; i <= param; i++) {
    if (i!==1)
        rs.add(folder+"_eooquiz-mongodb-node_" + i + ":27017");
}
cfg = rs.conf();
cfg.members[0].host = folder+"_eooquiz-mongodb-node_1:27017";
rs.reconfig(cfg);
