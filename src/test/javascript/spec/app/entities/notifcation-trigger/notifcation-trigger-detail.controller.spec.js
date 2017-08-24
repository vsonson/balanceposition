'use strict';

describe('Controller Tests', function() {

    describe('NotifcationTrigger Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNotifcationTrigger, MockTrigger;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNotifcationTrigger = jasmine.createSpy('MockNotifcationTrigger');
            MockTrigger = jasmine.createSpy('MockTrigger');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'NotifcationTrigger': MockNotifcationTrigger,
                'Trigger': MockTrigger
            };
            createController = function() {
                $injector.get('$controller')("NotifcationTriggerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:notifcationTriggerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
