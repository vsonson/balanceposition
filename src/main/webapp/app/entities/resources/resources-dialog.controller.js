(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('ResourcesDialogController', ResourcesDialogController);

    ResourcesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Resources'];

    function ResourcesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Resources) {
        var vm = this;

        vm.resources = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resources.id !== null) {
                Resources.update(vm.resources, onSaveSuccess, onSaveError);
            } else {
                Resources.save(vm.resources, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:resourcesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setIcon = function ($file, resources) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        resources.icon = base64Data;
                        resources.iconContentType = $file.type;
                    });
                });
            }
        };

    }
})();
