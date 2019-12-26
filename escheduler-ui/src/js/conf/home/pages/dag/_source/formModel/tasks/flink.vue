<template>
  <div class="flink-model">
    <m-list-box>
      <div slot="text">{{$t('Program Type')}}</div>
      <div slot="content">
        <x-select style="width: 130px;" v-model="programType" :disabled="isDetails">
          <x-option v-for="city in programTypeList" :key="city.code" :value="city.code" :label="city.code">
          </x-option>
        </x-select>
      </div>
    </m-list-box>

    <m-list-box v-if="programType !== 'PYTHON'">
      <div slot="text">{{$t('Main class')}}</div>
      <div slot="content">
        <x-input :disabled="isDetails" type="input" v-model="mainClass" :placeholder="$t('Please enter main class')" autocomplete="off">
        </x-input>
      </div>
    </m-list-box>
    <m-list-box>
      <div slot="text">{{$t('Main jar package')}}</div>
      <div slot="content">
        <x-select style="width: 100%;" :placeholder="$t('Please enter main jar package')" v-model="mainJar" filterable :disabled="isDetails">
          <x-option v-for="city in mainJarList" :key="city.code" :value="city.code" :label="city.code">
          </x-option>
        </x-select>
      </div>
    </m-list-box>
    <m-list-box>
      <div slot="text">{{$t('Deploy Mode')}}</div>
      <div slot="content">
        <x-radio-group v-model="deployMode">
          <x-radio :label="'cluster'" :disabled="isDetails"></x-radio>
        </x-radio-group>
      </div>
    </m-list-box>
    <div class="list-box-4p">
      <div class="clearfix list">
          <span class="sp1">{{$t('yarnSlots')}}</span>
          <span class="sp2">
            <x-input :disabled="isDetails" type="input" v-model="yarnSlots" :placeholder="$t('Please enter driver core number')" style="width: 190px;" autocomplete="off">
            </x-input>
          </span>
          <span class="sp1 sp3">{{$t('yarnContainer')}}</span>
          <span class="sp2">
            <x-input :disabled="isDetails" type="input" v-model="yarnContainer" :placeholder="$t('Please enter driver memory use')" style="width: 190px;" autocomplete="off">
            </x-input>
          </span>
      </div>

      <div class="clearfix list">
          <span class="sp1">{{$t('yarnName')}}</span>
          <span class="sp2">
            <x-input :disabled="isDetails" type="input" v-model="yarnName" :placeholder="$t('Please enter the task name')" style="width: 190px;" autocomplete="off" maxNumber='10'>
            </x-input>
          </span>
          <span class="sp1 sp3">{{$t('yarnQueue')}}</span>
          <span class="sp2">
            <x-select style="width: 190px;" v-model="yarnQueue" :disabled="isDetails">
              <x-option v-for="query in yarnQueueList" :key="query.queueName" :value="query.queueName" :label="query.queueName">
              </x-option>
            </x-select>
          </span>
      </div>
      <div class="clearfix list">
          <span class="sp1">{{$t('yarnJobManagerMemory')}}</span>
          <span class="sp2">
            <x-input :disabled="isDetails" type="input" v-model="yarnJobManagerMemory" :placeholder="$t('Please enter the number of Executor')" style="width: 190px;" autocomplete="off">
            </x-input>
          </span>
          <span class="sp1 sp3">{{$t('yarnTaskManagerMemory')}}</span>
          <span class="sp2">
            <x-input :disabled="isDetails" type="input" v-model="yarnTaskManagerMemory" :placeholder="$t('Please enter the Executor memory')" style="width: 190px;" autocomplete="off">
            </x-input>
          </span>
      </div>
    </div>
    <m-list-box>
      <div slot="text">{{$t('Command-line parameters')}}</div>
      <div slot="content">
        <x-input :autosize="{minRows:2}" :disabled="isDetails" type="textarea" v-model="mainArgs" :placeholder="$t('Please enter Command-line parameters')" autocomplete="off">
        </x-input>
      </div>
    </m-list-box>
    <m-list-box>
      <div slot="text">{{$t('Other parameters')}}</div>
      <div slot="content">
        <x-input :disabled="isDetails" :autosize="{minRows:2}" type="textarea" v-model="others" :placeholder="$t('Please enter other parameters')">
        </x-input>
      </div>
    </m-list-box>
    <m-list-box>
      <div slot="text">{{$t('Resources')}}</div>
      <div slot="content">
        <m-resources ref="refResources" @on-resourcesData="_onResourcesData" :resource-list="resourceList">
        </m-resources>
      </div>
    </m-list-box>
    <m-list-box>
      <div slot="text">{{$t('Custom Parameters')}}</div>
      <div slot="content">
        <m-local-params ref="refLocalParams" @on-local-params="_onLocalParams" :udp-list="localParams" :hide="false">
        </m-local-params>
      </div>
    </m-list-box>
  </div>
</template>
<script>
import _ from 'lodash';
import { mapActions } from 'vuex';
import i18n from '@/module/i18n';
import mLocalParams from './_source/localParams';
import mListBox from './_source/listBox';
import mResources from './_source/resources';
import disabledState from '@/module/mixin/disabledState';
export default {
  name: 'flink',
  data() {
    return {
      // Main function class
      mainClass: '',
      // Master jar package
      mainJar: null,
      // Master jar package(List)
      mainJarList: [],
      // Deployment method
      deployMode: 'cluster',
      // Resource(list)
      resourceList: [],
      // Custom function
      localParams: [],
      // Driver Number of cores
      yarnSlots: 1,
      // Driver Number of memory
      yarnContainer: '1',
      // Executor Number
      yarnJobManagerMemory: '1G',
      // Executor Number of memory
      yarnTaskManagerMemory: '2G',
      yarnName: '',
      yarnQueue: 'root.flink',
      // Command line argument
      mainArgs: '',
      // Other parameters
      others: '',
      // Program type
      programType: 'SCALA',
      // Program type(List)
      programTypeList: [{ code: 'JAVA' }, { code: 'SCALA' }, { code: 'PYTHON' }],
      yarnQueueList: []
    };
  },
  props: {
    backfillItem: Object
  },
  mixins: [disabledState],
  methods: {
    ...mapActions('security', ['getQueueListP']),
    /**
     * return localParams
     */
    _onLocalParams(a) {
      this.localParams = a;
    },
    /**
     * return resourceList
     */
    _onResourcesData(a) {
      this.resourceList = a;
    },
    /**
     * verification
     */
    _verification() {
      if (this.programType !== 'PYTHON' && !this.mainClass) {
        this.$message.warning(`${i18n.$t('Please enter main class')}`);
        return false;
      }
      if (!this.mainJar) {
        this.$message.warning(`${i18n.$t('Please enter main jar package')}`);
        return false;
      }
      if (!this.yarnJobManagerMemory) {
        this.$message.warning(`${i18n.$t('Please enter the number of Executor')}`);
        return false;
      }
      if (!Number.isInteger(parseInt(this.yarnJobManagerMemory))) {
        this.$message.warning(`${i18n.$t('The number of Executors should be a positive integer')}`);
        return false;
      }
      if (!this.yarnTaskManagerMemory) {
        this.$message.warning(`${i18n.$t('Please enter the Executor memory')}`);
        return false;
      }
      if (!_.isNumber(parseInt(this.yarnTaskManagerMemory))) {
        this.$message.warning(`${i18n.$t('Memory should be a positive integer')}`);
        return false;
      }
      if (!this.yarnName) {
        this.$message.warning(`${i18n.$t('Task name cannot be empty')}`);
        return false;
      }
      if (this.yarnName.length > 30) {
        this.$message.warning(`${i18n.$t('The name is limited to 30 characters')}`);
        return false;
      }
      if (!this.$refs.refResources._verifResources()) {
        return false;
      }
      // localParams Subcomponent verification
      if (!this.$refs.refLocalParams._verifProp()) {
        return false;
      }
      // storage
      this.$emit('on-params', {
        mainClass: this.mainClass,
        mainJar: {
          res: this.mainJar
        },
        deployMode: this.deployMode,
        resourceList: this.resourceList,
        localParams: this.localParams,
        yarnSlots: this.yarnSlots,
        yarnContainer: this.yarnContainer,
        yarnJobManagerMemory: this.yarnJobManagerMemory,
        yarnTaskManagerMemory: this.yarnTaskManagerMemory,
        yarnName: this.yarnName,
        yarnQueue: this.yarnQueue,
        mainArgs: this.mainArgs,
        others: this.others,
        programType: this.programType
      });
      return true;
    },
    /**
     * get resources list
     */
    _getResourcesList() {
      return new Promise((resolve, reject) => {
        let isJar = alias => {
          return alias.substring(alias.lastIndexOf('.') + 1, alias.length) !== 'jar';
        };
        this.mainJarList = _.map(_.cloneDeep(this.store.state.dag.resourcesListS), v => {
          return {
            id: v.id,
            code: v.alias,
            disabled: isJar(v.alias)
          };
        });
        resolve();
      });
    },
    _getYarnQueryList() {
      return new Promise((resolve, reject) => {
        this.store.dispatch('security/getQueueList').then(res => {
          this.yarnQueueList = res
          this.yarnQueue = res[0].queueName
          resolve();
        });
      });
    }
  },
  watch: {
    // Listening type
    programType(type) {
      if (type === 'PYTHON') {
        this.mainClass = '';
      }
    }
  },
  created() {
    this._getResourcesList().then(() => {
      let o = this.backfillItem;
      // Non-null objects represent backfill
      if (!_.isEmpty(o)) {
        this.mainClass = o.params.mainClass || '';
        this.mainJar = o.params.mainJar.res || '';
        this.deployMode = o.params.deployMode || '';
        this.yarnSlots = o.params.yarnSlots || 1;
        this.yarnContainer = o.params.yarnContainer || '1';
        this.yarnJobManagerMemory = o.params.yarnJobManagerMemory || '1G';
        this.yarnTaskManagerMemory = o.params.yarnTaskManagerMemory || '2G';
        this.yarnName = o.params.yarnName || 'aa';
        this.yarnQueue = o.params.yarnQueue || 'root.user.xuqiufeng';
        this.mainArgs = o.params.mainArgs || '';
        this.others = o.params.others;
        this.programType = o.params.programType || 'SCALA';
        // backfill resourceList
        let resourceList = o.params.resourceList || [];
        if (resourceList.length) {
          this.resourceList = resourceList;
        }
        // backfill localParams
        let localParams = o.params.localParams || [];
        if (localParams.length) {
          this.localParams = localParams;
        }
      }
    });
    this._getYarnQueryList();
  },
  mounted() {},
  components: { mLocalParams, mListBox, mResources }
};
</script>

<style lang="scss" rel="stylesheet/scss">
.flink-model {
  .list-box-4p {
    .list {
      margin-bottom: 14px;
        .sp1 {
          float: left;
          width: 140px;
          text-align: right;
          margin-right: 10px;
          font-size: 14px;
          color: #777;
          display: inline-block;
          padding-top: 6px;
        }
        .sp2 {
          float: left;
        }
      }
     
  }
}
</style>
