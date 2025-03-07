/**
 * 数据验证工具类
 */
export class DataValidator {
  constructor() {
    this.templates = {}
  }

  /**
   * 添加数据模板
   * @param {string} project - 项目名称
   * @param {string} type - 数据类型(app/h5)
   * @param {Object} template - 模板数据
   */
  addTemplate(project, type, template) {
    if (!this.templates[project]) {
      this.templates[project] = {}
    }
    if (!this.templates[project][type]) {
      this.templates[project][type] = []
    }
    this.templates[project][type].push(template)
  }

  /**
   * 获取项目支持的数据格式类型
   * @param {string} project - 项目名称
   * @returns {Array} 格式类型列表
   */
  getFormatTypes(project) {
    if (!this.templates[project]) {
      return []
    }
    return Object.keys(this.templates[project])
  }

  /**
   * 验证数据格式
   * @param {Object} data - 要验证的数据
   * @param {string} project - 项目名称
   * @param {string} type - 数据类型
   * @returns {Object} 验证结果
   */
  validateFormat(data, project, type) {
    if (!this.templates[project] || !this.templates[project][type]) {
      return {
        isValid: false,
        message: '未找到对应的数据模板',
        differences: {
          missing: [],
          extra: []
        }
      }
    }

    // 获取第一个模板作为验证标准
    const template = this.templates[project][type][0]
    const requiredFields = Object.entries(template)
      .filter(([_, value]) => value.required)
      .map(([key]) => key)

    const dataFields = Object.keys(data)
    const missingFields = requiredFields.filter(field => !dataFields.includes(field))
    const extraFields = dataFields.filter(field => !Object.keys(template).includes(field))

    const isValid = missingFields.length === 0 && extraFields.length === 0

    return {
      isValid,
      message: isValid ? '数据格式验证通过' : '数据格式验证未通过',
      differences: {
        missing: missingFields,
        extra: extraFields
      }
    }
  }
} 