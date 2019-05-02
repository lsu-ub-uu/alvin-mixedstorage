module se.uu.ub.cora.alvin.mixedstorage {
	requires transitive se.uu.ub.cora.sqldatabase;
	requires transitive se.uu.ub.cora.spider;
	requires transitive se.uu.ub.cora.httphandler;
	requires transitive java.xml;
	requires se.uu.ub.cora.gatekeeper;
	requires se.uu.ub.cora.logger;
	requires se.uu.ub.cora.basicstorage;

	exports se.uu.ub.cora.alvin.mixedstorage.db;
	exports se.uu.ub.cora.alvin.mixedstorage.fedora;
	exports se.uu.ub.cora.alvin.mixedstorage.id;
	exports se.uu.ub.cora.alvin.mixedstorage.parse;

	opens place;
}