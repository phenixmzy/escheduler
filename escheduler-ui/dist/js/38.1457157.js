webpackJsonp([38],{1157:function(e,a,n){"use strict";a.__esModule=!0;var t,i=n(750),s=(t=i)&&t.__esModule?t:{default:t};a.default={name:"security-index",components:{mSecondaryMenu:s.default}}},1291:function(e,a,n){"use strict";var t={render:function(){var e=this.$createElement,a=this._self._c||e;return a("div",{staticClass:"main-layout-box"},[a("m-secondary-menu",{attrs:{type:"security"}}),this._v(" "),a("router-view")],1)},staticRenderFns:[]};a.a=t},642:function(e,a,n){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var t=n(1157),i=n.n(t);for(var s in t)"default"!==s&&function(e){n.d(a,e,function(){return t[e]})}(s);var o=n(1291),d=n(25)(i.a,o.a,!1,null,null,null);a.default=d.exports},713:function(e,a,n){"use strict";a.__esModule=!0;var t,i=n(755),s=(t=i)&&t.__esModule?t:{default:t};a.default={name:"secondary-menu",data:function(){return{menuList:(0,s.default)(this.type),index:0,id:this.$route.params.id,isTogHide:!1}},props:{type:String,className:String},watch:{isTogHide:function(e){var a=$(".main-layout-box");e?a.addClass("toggle"):a.removeClass("toggle")}},methods:{_toggleSubMenu:function(e){e.isOpen=!e.isOpen},_toggleMenu:function(){this.isTogHide=!this.isTogHide}},mounted:function(){}}},750:function(e,a,n){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var t=n(713),i=n.n(t);for(var s in t)"default"!==s&&function(e){n.d(a,e,function(){return t[e]})}(s);var o=n(757);var d=function(e){n(751)},l=n(25)(i.a,o.a,!1,d,null,null);a.default=l.exports},751:function(e,a,n){var t=n(752);"string"==typeof t&&(t=[[e.i,t,""]]),t.locals&&(e.exports=t.locals);n(31)("e8b86f88",t,!0,{})},752:function(e,a,n){var t=n(126);(e.exports=n(30)(!1)).push([e.i,".main-layout-box.toggle{padding-left:0}.main-layout-box.toggle>.secondary-menu-model{left:-200px}.secondary-menu-model{position:fixed;left:0;top:0;width:200px;background:#41444c;height:100%;padding-top:80px}.secondary-menu-model .toogle-box{position:absolute;right:-1px;top:calc(50% - 50px)}.secondary-menu-model .toogle-box .tog-close{width:12px;height:102px;background:url("+t(n(753))+") no-repeat;display:inline-block}.secondary-menu-model .toogle-box .tog-open{width:12px;height:102px;background:url("+t(n(754))+") no-repeat;display:inline-block;position:absolute;right:-12px;top:0}.secondary-menu-model .leven-1 .name a{height:40px;line-height:40px;display:block;position:relative;padding-left:10px}.secondary-menu-model .leven-1 .name a>.icon{vertical-align:middle;font-size:15px;width:20px;text-align:center;color:#fff}.secondary-menu-model .leven-1 .name a>span{vertical-align:middle;padding-left:2px;font-size:14px;color:#fff}.secondary-menu-model .leven-1 .name a>.angle{position:absolute;right:12px;top:14px}.secondary-menu-model .leven-1 ul li{height:36px;line-height:36px;cursor:pointer;padding-left:39px;color:#fff}.secondary-menu-model .leven-1 ul li a{font-size:14px}.secondary-menu-model .leven-1 ul li.active{border-right:2px solid #2d8cf0;background:#2c2f39}.secondary-menu-model .leven-1 ul li.active span{font-weight:700;color:#2d8cf0}.secondary-menu-model .leven-1 .router-link-active,.secondary-menu-model .leven-1>.router-link-exact-active{background:#f0f6fb}.secondary-menu-model .leven-1 .router-link-active .name,.secondary-menu-model .leven-1>.router-link-exact-active .name{border-right:2px solid #2d8cf0;background:#2b2e38}.secondary-menu-model .leven-1 .router-link-active .name a span,.secondary-menu-model .leven-1>.router-link-exact-active .name a span{color:#2d8cf0;font-weight:700}.secondary-menu-model .leven-1 .router-link-active .name a .fa,.secondary-menu-model .leven-1>.router-link-exact-active .name a .fa{color:#2d8cf0}",""])},753:function(e,a,n){e.exports=n.p+"images/close.png?02806e641df25c1b4dbff4cb0af3984d"},754:function(e,a,n){e.exports=n.p+"images/open.png?97ec0726c7acab8a2a48282d68cea631"},755:function(e,a,n){"use strict";a.__esModule=!0;var t=o(n(33)),i=o(n(756)),s=o(n(125));function o(e){return e&&e.__esModule?e:{default:e}}var d={projects:[{name:""+t.default.$t("Project Home"),id:0,path:"projects-index",isOpen:!0,disabled:!0,icon:"fa-home",children:[]},{name:""+t.default.$t("Process"),id:1,path:"",isOpen:!0,disabled:!0,icon:"fa-gear",children:[{name:""+t.default.$t("Process definition"),path:"definition",id:0,disabled:!0},{name:""+t.default.$t("Process Instance"),path:"instance",id:1,disabled:!0},{name:""+t.default.$t("Task Instance"),path:"task-instance",id:2,disabled:!0},{name:""+t.default.$t("Task record"),path:"task-record",id:3,disabled:i.default.recordSwitch},{name:""+t.default.$t("History task record"),path:"history-task-record",id:4,disabled:i.default.recordSwitch}]}],security:[{name:""+t.default.$t("Tenant Manage"),id:0,path:"tenement-manage",isOpen:!0,disabled:!0,icon:"fa-users",children:[]},{name:""+t.default.$t("User Manage"),id:1,path:"users-manage",isOpen:!0,disabled:!0,icon:"fa-user-circle",children:[]},{name:""+t.default.$t("Warning manage"),id:2,path:"warning-groups-manage",isOpen:!0,disabled:!0,icon:"fa-warning",children:[]},{name:""+t.default.$t("Queue manage"),id:3,path:"queue-manage",isOpen:!0,disabled:!0,icon:"fa-recycle",children:[]},{name:""+t.default.$t("Worker group manage"),id:4,path:"worker-groups-manage",isOpen:!0,disabled:!0,icon:"fa-address-book",children:[]},{name:""+t.default.$t("Token manage"),id:2,path:"token-manage",isOpen:!0,icon:"fa-file-text",children:[],disabled:!0}],resource:[{name:""+t.default.$t("File Manage"),id:0,path:"file",isOpen:!0,icon:"fa-files-o",children:[],disabled:!0},{name:""+t.default.$t("UDF manage"),id:1,path:"",isOpen:!0,icon:"fa-file-text",disabled:!0,children:[{name:""+t.default.$t("Resource manage"),path:"resource-udf-resource",id:0,disabled:!0},{name:""+t.default.$t("Function manage"),path:"resource-udf-function",id:1,disabled:!0}]}],user:[{name:""+t.default.$t("User Information"),id:0,path:"account",isOpen:!0,icon:"fa-user",children:[],disabled:!0},{name:""+t.default.$t("Edit password"),id:1,path:"password",isOpen:!0,icon:"fa-key",children:[],disabled:!0},{name:""+t.default.$t("Token manage"),id:2,path:"token",isOpen:!0,icon:"fa-file-text",children:[],disabled:s.default.getAuth()}],monitor:[{name:""+t.default.$t("Servers manage"),id:1,path:"",isOpen:!0,disabled:!0,icon:"fa-server",children:[{name:"Master",path:"servers-master",id:0,disabled:!0},{name:"Worker",path:"servers-worker",id:1,disabled:!0},{name:"Zookeeper",path:"servers-zookeeper",id:4,disabled:!0},{name:"Mysql",path:"servers-mysql",id:6,disabled:!0}]},{name:""+t.default.$t("Statistics manage"),id:0,path:"",isOpen:!0,disabled:!0,icon:"fa-server",children:[{name:"Statistics",path:"statistics",id:0,disabled:!0}]}],gantt:[{name:""+t.default.$t("Scheduling operations"),id:1,path:"",isOpen:!0,disabled:!0,icon:"fa-gear",children:[{name:""+t.default.$t("Workflow orchestration"),path:"workflow",id:0,disabled:!0}]}]};a.default=function(e){return d[e]}},756:function(e,a,n){"use strict";a.__esModule=!0,a.default={recordSwitch:!1}},757:function(e,a,n){"use strict";var t={render:function(){var e=this,a=e.$createElement,n=e._self._c||a;return n("div",{staticClass:"secondary-menu-model",class:e.className},[n("div",{staticClass:"toogle-box"},[e.isTogHide?e._e():n("a",{staticClass:"tog-close",attrs:{href:"javascript:"},on:{click:e._toggleMenu}}),e._v(" "),e.isTogHide?n("a",{staticClass:"tog-open",attrs:{href:"javascript:"},on:{click:e._toggleMenu}}):e._e()]),e._v(" "),e._l(e.menuList,function(a,t){return n("div",{staticClass:"leven-1"},[a.disabled?n("div",[a.path?[n("router-link",{attrs:{to:{name:a.path}}},[n("div",{staticClass:"name",on:{click:function(n){return e._toggleSubMenu(a)}}},[n("a",{attrs:{href:"javascript:"}},[n("i",{staticClass:"fa icon",class:a.icon}),e._v(" "),n("span",[e._v(e._s(a.name))]),e._v(" "),a.children.length?n("i",{staticClass:"fa angle",class:a.isOpen?"fa-angle-down":"fa-angle-right"}):e._e()])])])]:e._e(),e._v(" "),a.path?e._e():[n("div",{staticClass:"name",on:{click:function(n){return e._toggleSubMenu(a)}}},[n("a",{attrs:{href:"javascript:"}},[n("i",{staticClass:"fa icon",class:a.icon}),e._v(" "),n("span",[e._v(e._s(a.name))]),e._v(" "),a.children.length?n("i",{staticClass:"fa angle",class:a.isOpen?"fa-angle-down":"fa-angle-right"}):e._e()])])],e._v(" "),a.isOpen&&a.children.length?n("ul",[e._l(a.children,function(a,t){return[a.disabled?n("router-link",{attrs:{to:{name:a.path},tag:"li","active-class":"active"}},[n("span",[e._v(e._s(a.name))])]):e._e()]})],2):e._e()],2):e._e()])})],2)},staticRenderFns:[]};a.a=t}});
//# sourceMappingURL=38.1457157.js.map