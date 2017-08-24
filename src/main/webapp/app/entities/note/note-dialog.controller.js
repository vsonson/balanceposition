(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('NoteDialogController', NoteDialogController);

    NoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Note', 'UserInfo', 'TrackMetric', 'ProgramStep'];

    function NoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Note, UserInfo, TrackMetric, ProgramStep) {
        var vm = this;

        vm.note = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.userinfos = UserInfo.query();
        vm.trackmetrics = TrackMetric.query({filter: 'note-is-null'});
        $q.all([vm.note.$promise, vm.trackmetrics.$promise]).then(function() {
            if (!vm.note.trackMetric || !vm.note.trackMetric.id) {
                return $q.reject();
            }
            return TrackMetric.get({id : vm.note.trackMetric.id}).$promise;
        }).then(function(trackMetric) {
            vm.trackmetrics.push(trackMetric);
        });
        vm.programsteps = ProgramStep.query({filter: 'note-is-null'});
        $q.all([vm.note.$promise, vm.programsteps.$promise]).then(function() {
            if (!vm.note.programStep || !vm.note.programStep.id) {
                return $q.reject();
            }
            return ProgramStep.get({id : vm.note.programStep.id}).$promise;
        }).then(function(programStep) {
            vm.programsteps.push(programStep);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.note.id !== null) {
                Note.update(vm.note, onSaveSuccess, onSaveError);
            } else {
                Note.save(vm.note, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:noteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
