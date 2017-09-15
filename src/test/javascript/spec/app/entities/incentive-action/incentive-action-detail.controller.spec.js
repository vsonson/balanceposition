'use strict';

describe('Controller Tests', function() {

    describe('IncentiveAction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockIncentiveAction, MockTrigger, MockIncentive;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockIncentiveAction = jasmine.createSpy('MockIncentiveAction');
            MockTrigger = jasmine.createSpy('MockTrigger');
            MockIncentive = jasmine.createSpy('MockIncentive');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'IncentiveAction': MockIncentiveAction,
                'Trigger': MockTrigger,
                'Incentive': MockIncentive
            };
            createController = function() {
                $injector.get('$controller')("IncentiveActionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:incentiveActionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
