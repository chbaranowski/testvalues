package testvalues

class ParameterBuilder {

    TestDataModel model = new TestDataModel()

    def call(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = this
        closure()
    }

    def params(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = new ParamsBuilder(model: model.params)
        closure()
    }

    def constraint(Closure closure) {
        model.constraints << closure
    }

    def getValidValues() {
        def validValues = []
        def maxValidValues = ((model.params*.validValues)*.size()).max() - 1
        (0..maxValidValues).each { i ->
            def testSet = [:]
            model.params.each { param ->
                if (i < param.validValues.size()) {
                    testSet[param.name] = param.validValues[i]
                } else {
                    testSet[param.name] = param.validValues[0]
                }
            }

            validValues.add(testSet)
        }
        validValues
    }

    def getInvalidValues() {
        def invalidValues = []
        model.params.each { invalidParam ->
            invalidParam.invalidValues.each { invalidValue ->
                def invalidDataSet = [:]
                model.params.findAll { it != invalidParam }.each { validParam ->
                    invalidDataSet[validParam.name] = validParam.validValues[0]
                }
                invalidDataSet[invalidParam.name] = invalidValue
                model.params.helpers.each { helper ->
                    def model = [name: invalidParam.name, value: invalidValue]
                    Closure helperClosure = helper.value
                    helperClosure.resolveStrategy = Closure.DELEGATE_FIRST
                    helperClosure.delegate = model
                    invalidDataSet[helper.key] = helperClosure()
                }
                invalidValues.add(invalidDataSet)
            }
        }
        invalidValues
    }

    def getParams() {
        new ParamsFinder(model: model.params)
    }

}


class ParamsFinder {

    Parameters model

    Object getProperty(String name){
       model.parameters.find{it.name == name}
    }
}

class ParamsBuilder {

    Parameters model

    public Object invokeMethod(String name, args) {
        if (args.length > 1 && args[0] instanceof Map && args[1] instanceof Closure) {
            param(name, (Map) args[0], (Closure) args[1])
        } else if (args.length == 1 && args[0] instanceof Closure) {
            helper(name, (Closure) args[0])
        } else {
            throw new RuntimeException("No valid parameter definition with name $name and args $args")
        }
    }

    def param(String name, Map attributes, Closure closure) {
        def typeName = attributes.type
        def type = ParameterFactory.getInstance().types[typeName]
        Parameter parameterModel = type.newInstance()
        parameterModel.name = name
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = parameterModel
        closure()
        model.add(parameterModel)
    }

    def helper(String name, Closure closure) {
        model.helpers[name] = closure
    }

}

