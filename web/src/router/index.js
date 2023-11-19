import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '@/views/pk/PkIndexView.vue'
import RecordIndexView from '@/views/record/RecordIndexView.vue'
import RankListIndexView from '@/views/ranklist/RankListIndexView.vue'
import UserBotsIndexView from '@/views/user/bots/UserBotsIndexView.vue'
import NotFound from '@/views/erro/NotFound.vue'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView.vue'
import store from '@/store/index'

const routes = [
  {
    path:'/',
    name:'home',
    redirect:'/pk/',
    meta:{
      requestAuth: true,
    }
  },
  {
    path:'/:catchAll(.*)',
    redirect:'/404/',
  },
  {
    path:'/pk/',
    name:'pk_index',
    component:PkIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:'/record/',
    name:'record_index',
    component:RecordIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:'/ranklist/',
    name:'ranklist_index',
    component:RankListIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:'/user/bot/',
    name:'user_bot_index',
    component:UserBotsIndexView,
    meta:{
      requestAuth: true,
    }
  },
  {
    path:'/404/',
    name:'404',
    component:NotFound,
  },
  {
    path:'/user/account/login/',
    name:'user_account_login',
    component:UserAccountLoginView,
  },
  {
    path:'/user/account/register/',
    name:'user_account_register',
    component:UserAccountRegisterView,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to,from,next) => {//每次router执行前，会先调用这个函数
  if(to.meta.requestAuth && !store.state.user.is_login){
    next({name:"user_account_login"});
  }else{
    next();
  }

})

export default router
