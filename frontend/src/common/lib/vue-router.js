import { createRouter, createWebHistory } from 'vue-router'
import Main from '@/views/main/components/main-content.vue'
import Login from '@/views/main/components/login-dialog.vue'
import Mypage from '@/views/user/mypage.vue'
import Conference from '@/views/main/components/conference.vue'
import KakaoCallback from '@/views/main/components/kakao-callback.vue'
import Conferenceroom from '@/views/conferences/conferenceroom.vue'

const routes = [
  {
    path: '/',
    name: 'Main',
    component: Main,
  },
  {
    path: '/conference',
    name: 'conference',
    component: Conference,
  },
  {
    path: '/conference/room',
    name: 'conferenceroom',
    component: Conferenceroom,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path : '/kakao/callback',
    name: 'KakaoCallback',
    component : KakaoCallback,
  },
  {
    path: '/mypage',
    name: 'Mypage',
    component: Mypage,
    children: [
      {
        path: "profile",
        name: "mypage-profile",
        component: () => import("@/views/user/components/user-profile.vue"),
      },
      {
        path: "mypost",
        name: "mypage-mypost",
        component: () => import("@/views/user/components/user-post-list.vue"),
      },
      {
        path: "applyList",
        name: "mypage-apply-list",
        component: () => import("@/views/user/components/user-apply-list.vue"),
      },
      {
        path: "applyResultList",
        name: "mypage-result-list",
        component: () => import("@/views/user/components/user-apply-result-list.vue"),
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
