<template>
  <div class="conditions-model">
    <div class="left">
      <slot name="button-group"></slot>
    </div>
    <div class="right">
      <div class="from-box">
        <slot name="search-group" v-if="isShow"></slot>
        <template v-if="!isShow">
          <div class="list">
            <x-button type="ghost"  @click="_ckQuery" icon="fa fa-search"></x-button>
          </div>
          <div class="list" >
            <x-input v-model="searchVal"
                     @on-enterkey="_ckQuery"
                     :placeholder="$t('Please enter keyword')"
                     type="text"
                     style="width:180px;">
            </x-input>
          </div>
          <div class="list" v-if='StateShow'>
            <x-select :placeholder="$t('State')" style="width: 157px;" v-model="releaseState">
              <x-option  v-for="item in optionList" :key='item.value' :value="item.value" :label="item.label"></x-option>
            </x-select>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>
<script>
  import _ from 'lodash'
  export default {
    name: 'conditions',
    data () {
      return {
        // search value
        searchVal: '',
        releaseState:'',
        optionList:[
          {value:'',label:'-全选-'},
          {value:'0',label:$t('offline')},
          {value:'1',label:$t('online')}
        ],
        StateShow:false
      }
    },
    props: {
      operation: Array
    },
    methods: {
      /**
       * emit Query parameter
       */
      _ckQuery () {
        this.$emit('on-conditions', {
          searchVal: _.trim(this.searchVal),
          releaseState:this.releaseState
        })
      }
    },
    computed: {
      // Whether the slot comes in
      isShow () {
        return this.$slots['search-group']
      }
    },
    created () {
      // Routing parameter merging
      if(this.$route.name === 'projects-definition-list'){
        this.StateShow = true
      }
      if (!_.isEmpty(this.$route.query)) {
        this.searchVal = this.$route.query.searchVal || ''
        this.releaseState = this.$route.query.releaseState || ''
      }
    },
    components: {}
  }
</script>
