'use strict';

describe('Controller Tests', function() {

    describe('ProgramHistory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProgramHistory, MockProgramLevel, MockProgramStep, MockUserInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProgramHistory = jasmine.createSpy('MockProgramHistory');
            MockProgramLevel = jasmine.createSpy('MockProgramLevel');
            MockProgramStep = jasmine.createSpy('MockProgramStep');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProgramHistory': MockProgramHistory,
                'ProgramLevel': MockProgramLevel,
                'ProgramStep': MockProgramStep,
                'UserInfo': MockUserInfo
            };
            createController = function() {
                $injector.get('$controller')("ProgramHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:programHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
