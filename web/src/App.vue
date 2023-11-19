<template>
  <navbar></navbar>
  <router-view/>
</template>

<script>
import $ from 'jquery';
import { ref } from 'vue';
import navbar from './components/NavBar.vue';
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"

export default {
  name : "App",
  components:{
    navbar,
  }
}
</script>

<style>
body{
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
</style>
