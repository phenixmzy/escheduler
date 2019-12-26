<template>
  <m-popup
          ref="popup"
          :ok-text="item ? $t('Edit') : $t('Submit')"
          :nameText="item ? $t('Edit alarm group') : $t('Create alarm group')"
          @ok="_ok">
    <template slot="content">
      <div class="create-warning-model">
        <m-list-box-f>
          <template slot="name"><b>*</b>{{$t('Group Name')}}</template>
          <template slot="content">
            <x-input
                    type="input"
                    v-model="groupName"
                    :placeholder="$t('Please enter group name')">
            </x-input>
          </template>
        </m-list-box-f>
        <m-list-box-f>
          <template slot="name"><b>*</b>{{$t('Group Type')}}</template>
          <template slot="content">
            <x-select multiple v-model="groupType">
              <x-option
                      v-for="city in options"
                      :key="city.id"
                      :value="city.id"
                      :label="city.code">
              </x-option>
            </x-select>
          </template>
        </m-list-box-f>
        <m-list-box-f>
          <template slot="name">{{$t('Remarks')}}</template>
          <template slot="content">
            <x-input
                    type="textarea"
                    v-model="desc"
                    :placeholder="$t('Please enter description')">
            </x-input>
          </template>
        </m-list-box-f>
      </div>
    </template>
  </m-popup>
</template>
<script>
  import i18n from '@/module/i18n'
  import store from '@/conf/home/store'
  import mPopup from '@/module/components/popup/popup'
  import mListBoxF from '@/module/components/listBoxF/listBoxF'

  export default {
    name: 'create-warning',
    data () {
      return {
        store,
        groupName: '',
        groupType: ['vv', 'weixin','email'],
        desc: '',
        options: [{ code: `vv`, id: 'vv' }, { code: `weixin`, id: 'weixin' },{ code: `email`, id: 'email' }]
      }
    },
    props: {
      item: Object
    },
    methods: {
      _ok () {
        if (this._verification()) {
          // The name is not verified
          if (this.item && this.item.groupName === this.groupName) {
            this._submit()
            return
          }

          // Verify username
          this.store.dispatch(`security/verifyName`, {
            type: 'alertgroup',
            groupName: this.groupName
          }).then(res => {
            this._submit()
          }).catch(e => {
            this.$message.error(e.msg || '')
          })
        }
      },
      _verification () {
        // group name
        if (!this.groupName) {
          this.$message.warning(`${i18n.$t('Please enter group name')}`)
          return false
        }
        if (!this.groupType) {
          this.$message.warning(`${i18n.$t('Please enter group type')}`)
          return false
        }
        return true
      },
      _submit () {
        let param = {
          groupName: this.groupName,
          groupType: this.groupType,
          desc: this.desc
        }
        if (this.item) {
          param.id = this.item.id
        }
        this.$refs['popup'].spinnerLoading = true
        this.store.dispatch(`security/${this.item ? 'updateAlertgrou' : 'createAlertgrou'}`, param).then(res => {
          this.$emit('onUpdate')
          this.$message.success(res.msg)
          setTimeout(() => {
            this.$refs['popup'].spinnerLoading = false
          }, 800)
        }).catch(e => {
          this.$message.error(e.msg || '')
          this.$refs['popup'].spinnerLoading = false
        })
      }
    },
    watch: {},
    created () {
      if (this.item) {
        this.groupName = this.item.groupName
        this.groupType = this.item.groupType.split(',')
        this.desc = this.item.desc
      }
    },
    mounted () {
    },
    components: { mPopup, mListBoxF }
  }
</script>
