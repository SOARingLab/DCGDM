import Vue from 'vue'
import VueRouter from 'vue-router'
import { Message }from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import Login from '../views/Login';
import Welcome from "../views/Welcome";
import Home from '../views/Home.vue';

import UserInfo from "../views/user/UserInfo";
import RoleInfo from "../views/role/RoleInfo";
import ResourceInfo from "../views/role/ResourceInfo";
import Error404 from "../views/Error404";
import PubSub from "@/views/pubsub/pubSub.vue";
import Topic from "@/views/topic/topic.vue";
import Engine from "@/views/engine/engine.vue";
import Process from "@/views/process/process.vue";
import Map_sh from "@/views/map/map.vue"
import Cmb from "@/views/cmb/map.vue"
import Supermarket from "@/views/cbpmDemo/Supermarket.vue";
import Supplier from "@/views/cbpmDemo/Supplier.vue";
import Logistics from "@/views/cbpmDemo/Logistics.vue";
import SupermarketNeedAlliance from "@/views/cbpmDemo/SupermarketNeedAlliance.vue";
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Login,
    redirect: '/login'
  },
  {
    path: '/login',
    component: Login
  },

  {
    path: '/map',
    component: Map_sh
  },
  {
    path: '/cmb',
    component: Cmb
  },
  {
    path: '/superV2',
    component: SupermarketNeedAlliance
  },
  {
    path: '/supermarket',
    component: Supermarket
  },
  {
    path: '/supplier',
    component: Supplier
  },
  {
    path: '/logistics',
    component: Logistics
  },
  {
    path: '/home',
    component: Home,
    redirect: '/welcome',
    children: [
      { path: '/welcome', component: Welcome },


      { path: '/user', component: UserInfo},
      { path: '/role', component: RoleInfo},
      { path: '/resource', component: ResourceInfo},
      { path: '/pub', component: PubSub},
      { path: '/topic', component: Topic},
      { path: '/engine', component: Engine},
      { path: '/process', component: Process},
    ]
  },
  {
    path: '/*',
    component: Error404
  }
]

const router = new VueRouter({
  routes,
  mode: 'history',
})

//挂载路由导航守卫
router.beforeEach((to, from, next) =>{
  //to 将要访问的路径
  //from 从哪个页面来
  //next 一个放行函数

  if(to.path === '/login' || to.path === '/404' || to.path === '/map'  || to.path === '/cmb'
    || to.path === '/supermarket' || to.path === '/supplier'|| to.path === '/logistics'
    || to.path === '/superV2') return next();

  //顶部进度条
  NProgress.start()
  //获取token
  const token = window.sessionStorage.getItem("token")
  if(!token){
    Message.error('抱歉，请先登录')
    return next('/login');
  }
  next();
})

router.afterEach(() => {
  NProgress.done()
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location){
  return originalPush.call(this, location).catch(err => err)
}
//

export default router
