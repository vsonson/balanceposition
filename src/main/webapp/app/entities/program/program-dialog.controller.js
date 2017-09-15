(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ProgramDialogController', ProgramDialogController);

    ProgramDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Program', 'ProgramLevel'];

    function ProgramDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Program, ProgramLevel) {
        var vm = this;

        vm.program = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.programlevels = ProgramLevel.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.program.id !== null) {
                Program.update(vm.program, onSaveSuccess, onSaveError);
            } else {
                Program.save(vm.program, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:programUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setIcon = function ($file, program) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        program.icon = base64Data;
                        program.iconContentType = $file.type;
                    });
                });
            }
        };

    }
})();
