'use strict';

describe('Controller Tests', function() {

    describe('PathStep Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPathStep, MockPathAction, MockPathWay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPathStep = jasmine.createSpy('MockPathStep');
            MockPathAction = jasmine.createSpy('MockPathAction');
            MockPathWay = jasmine.createSpy('MockPathWay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PathStep': MockPathStep,
                'PathAction': MockPathAction,
                'PathWay': MockPathWay
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
