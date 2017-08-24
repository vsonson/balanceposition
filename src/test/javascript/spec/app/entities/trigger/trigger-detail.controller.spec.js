'use strict';

describe('Controller Tests', function() {

    describe('Trigger Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTrigger, MockNotifcationTrigger, MockWellnessItem, MockIncentiveAction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTrigger = jasmine.createSpy('MockTrigger');
            MockNotifcationTrigger = jasmine.createSpy('MockNotifcationTrigger');
            MockWellnessItem = jasmine.createSpy('MockWellnessItem');
            MockIncentiveAction = jasmine.createSpy('MockIncentiveAction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Trigger': MockTrigger,
                'NotifcationTrigger': MockNotifcationTrigger,
                'WellnessItem': MockWellnessItem,
                'IncentiveAction': MockIncentiveAction
            };
            createController = function() {
                $injector.get('$controller')("TriggerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:triggerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
