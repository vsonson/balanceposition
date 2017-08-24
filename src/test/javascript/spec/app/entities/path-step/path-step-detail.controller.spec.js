'use strict';

describe('Controller Tests', function() {

    describe('PathStep Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPathStep, MockPathWay, MockPathAction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPathStep = jasmine.createSpy('MockPathStep');
            MockPathWay = jasmine.createSpy('MockPathWay');
            MockPathAction = jasmine.createSpy('MockPathAction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PathStep': MockPathStep,
                'PathWay': MockPathWay,
                'PathAction': MockPathAction
            };
            createController = function() {
                $injector.get('$controller')("PathStepDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:pathStepUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
