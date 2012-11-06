"use strict";
/** @suppress {duplicate}*/var protobuf;
if (typeof(protobuf)=="undefined") {protobuf = {};}

protobuf.Command = PROTO.Message("protobuf.Command",{
	Module: PROTO.Enum("protobuf.Command.Module",{
		NONE :1,
		CONSTANT :2,
		CURSOR :3,
		FADE :4,
		RANDOM :5,
		SCREEN :6	}),
	module: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return protobuf.Command.Module;},
		id: 1
	},
	Action: PROTO.Enum("protobuf.Command.Action",{
		START :1,
		STOP :2,
		PAUSE :3,
		RESUME :4	}),
	action: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return protobuf.Command.Action;},
		id: 2
	},
	interval: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 3
	},
	red: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 4
	},
	green: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 5
	},
	blue: {
		options: {},
		multiplicity: PROTO.optional,
		type: function(){return PROTO.int32;},
		id: 6
	}});
