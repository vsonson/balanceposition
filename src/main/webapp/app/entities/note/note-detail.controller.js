(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('NoteDetailController', NoteDetailController);

    NoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Note', 'UserInfo', 'TrackMetric', 'ProgramStep'];

    function NoteDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Note, UserInfo, TrackMetric, ProgramStep) {
        var vm = this;

        vm.note = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('balancepositionApp:noteUpdate', function(event, result) {
            vm.note = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
