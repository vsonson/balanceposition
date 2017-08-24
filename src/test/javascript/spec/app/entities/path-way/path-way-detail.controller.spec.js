'use strict';

describe('Controller Tests', function() {

    describe('PathWay Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPathWay, MockUserNotification, MockPathStep;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPathWay = jasmine.createSpy('MockPathWay');
            MockUserNotification = jasmine.createSpy('MockUserNotification');
            MockPathStep = jasmine.createSpy('MockPathStep');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PathWay': MockPathWay,
                'UserNotification': MockUserNotification,
                'PathStep': MockPathStep
            };
            createController = function() {
                $injector.get('$controller')("PathWayDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:pathWayUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
