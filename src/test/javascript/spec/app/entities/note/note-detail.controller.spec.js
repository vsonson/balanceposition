'use strict';

describe('Controller Tests', function() {

    describe('Note Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNote, MockTrackMetric, MockProgramStep, MockUserInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNote = jasmine.createSpy('MockNote');
            MockTrackMetric = jasmine.createSpy('MockTrackMetric');
            MockProgramStep = jasmine.createSpy('MockProgramStep');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Note': MockNote,
                'TrackMetric': MockTrackMetric,
                'ProgramStep': MockProgramStep,
                'UserInfo': MockUserInfo
            };
            createController = function() {
                $injector.get('$controller')("NoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:noteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
