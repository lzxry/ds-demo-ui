<template>
  <div class="data-validation">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据校验</span>
          <div class="header-right">
            <el-select v-model="project" placeholder="选择项目" style="width: 120px">
              <el-option label="拼多多" value="pdd" />
              <el-option label="京东" value="jd" />
              <el-option label="淘宝" value="tb" />
            </el-select>
            <el-select v-model="dataType" placeholder="数据类型" style="width: 120px; margin-left: 10px">
              <el-option
                v-for="type in formatTypes"
                :key="type"
                :label="type === 'app' ? 'APP数据' : 'H5数据'"
                :value="type"
              />
            </el-select>
            <el-button type="primary" style="margin-left: 10px" @click="handleValidate">
              校验格式
            </el-button>
          </div>
        </div>
      </template>

      <!-- 数据格式列表 -->
      <div class="format-list" v-if="formatTypes.length > 0">
        <h3>当前项目支持的数据格式：</h3>
        <div class="format-types">
          <el-tag
            v-for="type in formatTypes"
            :key="type"
            type="info"
            style="margin-right: 10px; margin-bottom: 10px"
          >
            {{ type === 'app' ? 'APP数据' : 'H5数据' }}
          </el-tag>
        </div>
        
        <div class="template-previews">
          <h4>数据模板预览：</h4>
          <div class="preview-cards">
            <el-card
              v-for="(template, index) in templates"
              :key="index"
              class="preview-card"
              shadow="hover"
            >
              <template #header>
                <div class="preview-header">
                  <span>模板 {{ index + 1 }}</span>
                  <el-button type="primary" link @click="handleViewTemplate(template)">
                    查看详情
                  </el-button>
                </div>
              </template>
              <div class="preview-content">
                <pre>{{ formatJsonPreview(template) }}</pre>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 模板详情弹窗 -->
    <el-dialog
      v-model="templateDetailVisible"
      title="模板详情"
      width="800px"
      destroy-on-close
    >
      <div class="template-detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="字段名" v-for="(field, key) in currentTemplate" :key="key">
            <div class="field-info">
              <span class="field-name">{{ key }}</span>
              <el-tag size="small" :type="field.required ? 'danger' : 'info'" style="margin-left: 10px">
                {{ field.required ? '必需' : '可选' }}
              </el-tag>
            </div>
            <div class="field-description">{{ field.description }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 校验弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="数据格式校验"
      width="800px"
      destroy-on-close
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="JSON数据">
          <el-input
            v-model="form.jsonData"
            type="textarea"
            :rows="10"
            placeholder="请输入要校验的JSON数据"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            开始校验
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 校验结果弹窗 -->
    <el-dialog
      v-model="resultVisible"
      title="校验结果"
      width="600px"
      destroy-on-close
    >
      <div class="result-content">
        <el-alert
          :title="validationResult.message"
          :type="validationResult.isValid ? 'success' : 'warning'"
          :closable="false"
          show-icon
        />
        
        <div v-if="!validationResult.isValid" class="differences">
          <div v-if="validationResult.differences.missing.length > 0" class="difference-section">
            <h4>缺失字段：</h4>
            <el-tag
              v-for="field in validationResult.differences.missing"
              :key="field"
              type="danger"
              style="margin-right: 10px; margin-bottom: 10px"
            >
              {{ field }}
            </el-tag>
          </div>
          
          <div v-if="validationResult.differences.extra.length > 0" class="difference-section">
            <h4>多余字段：</h4>
            <el-tag
              v-for="field in validationResult.differences.extra"
              :key="field"
              type="warning"
              style="margin-right: 10px; margin-bottom: 10px"
            >
              {{ field }}
            </el-tag>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { DataValidator } from '@/utils/dataValidator'

console.log('DataValidation component mounted')

const project = ref('pdd')
const dataType = ref('app')
const dialogVisible = ref(false)
const resultVisible = ref(false)
const form = ref({
  jsonData: ''
})

const validationResult = ref({
  isValid: false,
  message: '',
  differences: {
    missing: [],
    extra: []
  }
})

// 创建数据验证器实例
const validator = new DataValidator()

// 模拟模板数据
const mockTemplates = [
  {
    id: {
      required: true,
      description: '数据唯一标识'
    },
    title: {
      required: true,
      description: '商品标题'
    },
    price: {
      required: true,
      description: '商品价格'
    }
  },
  {
    id: {
      required: true,
      description: '数据唯一标识'
    },
    title: {
      required: true,
      description: '商品标题'
    },
    price: {
      required: true,
      description: '商品价格'
    },
    description: {
      required: false,
      description: '商品描述'
    }
  }
]

// 添加模板
mockTemplates.forEach(template => {
  validator.addTemplate('pdd', 'app', template)
})

const templates = ref(mockTemplates)
const templateDetailVisible = ref(false)
const currentTemplate = ref({})

// 获取当前项目支持的数据格式类型
const formatTypes = computed(() => {
  console.log('Computing format types for project:', project.value)
  return validator.getFormatTypes(project.value)
})

// 格式化JSON预览
const formatJsonPreview = (template) => {
  const preview = {}
  Object.entries(template).forEach(([key, value]) => {
    preview[key] = value.required ? '必需' : '可选'
  })
  return JSON.stringify(preview, null, 2)
}

// 查看模板详情
const handleViewTemplate = (template) => {
  currentTemplate.value = template
  templateDetailVisible.value = true
}

// 打开校验弹窗
const handleValidate = () => {
  dialogVisible.value = true
}

// 提交校验
const handleSubmit = () => {
  try {
    const data = JSON.parse(form.value.jsonData)
    if (typeof data !== 'object' || data === null) {
      throw new Error('请输入有效的JSON对象数据')
    }
    
    // 执行校验
    validationResult.value = validator.validateFormat(data, project.value, dataType.value)
    resultVisible.value = true
  } catch (error) {
    ElMessage.error(`数据格式错误：${error.message}`)
  }
}

onMounted(() => {
  console.log('DataValidation component mounted')
})
</script>

<style scoped lang="scss">
.data-validation {
  .header-right {
    display: flex;
    align-items: center;
  }

  .format-list {
    margin-top: 20px;

    .format-types {
      margin-bottom: 20px;
    }

    .template-previews {
      h4 {
        margin-bottom: 15px;
        color: #606266;
      }

      .preview-cards {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 20px;

        .preview-card {
          .preview-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
          }

          .preview-content {
            pre {
              margin: 0;
              white-space: pre-wrap;
              word-wrap: break-word;
              font-size: 12px;
              color: #606266;
              background-color: #f5f7fa;
              padding: 10px;
              border-radius: 4px;
            }
          }
        }
      }
    }
  }

  .template-detail {
    .field-info {
      display: flex;
      align-items: center;
      margin-bottom: 5px;

      .field-name {
        font-weight: bold;
        color: #303133;
      }
    }

    .field-description {
      color: #909399;
      font-size: 13px;
    }
  }

  .result-content {
    .differences {
      margin-top: 20px;
      
      .difference-section {
        margin-bottom: 20px;
        
        h4 {
          margin-bottom: 10px;
          color: #606266;
        }
      }
    }
  }
}
</style> 