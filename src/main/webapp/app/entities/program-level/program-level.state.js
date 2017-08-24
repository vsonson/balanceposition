(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('program-level', {
            parent: 'entity',
            url: '/program-level?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProgramLevels'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program-level/program-levels.html',
                    controller: 'ProgramLevelController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('program-level-detail', {
            parent: 'program-level',
            url: '/program-level/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProgramLevel'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/program-level/program-level-detail.html',
                    controller: 'ProgramLevelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProgramLevel', function($stateParams, ProgramLevel) {
                    return ProgramLevel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'program-level',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('program-level-detail.edit', {
            parent: 'program-level-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-level/program-level-dialog.html',
                    controller: 'ProgramLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProgramLevel', function(ProgramLevel) {
                            return ProgramLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program-level.new', {
            parent: 'program-level',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-level/program-level-dialog.html',
                    controller: 'ProgramLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                isLocked: false,
                                isPaid: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('program-level', null, { reload: 'program-level' });
                }, function() {
                    $state.go('program-level');
                });
            }]
        })
        .state('program-level.edit', {
            parent: 'program-level',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-level/program-level-dialog.html',
                    controller: 'ProgramLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProgramLevel', function(ProgramLevel) {
                            return ProgramLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program-level', null, { reload: 'program-level' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('program-level.delete', {
            parent: 'program-level',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/program-level/program-level-delete-dialog.html',
                    controller: 'ProgramLevelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProgramLevel', function(ProgramLevel) {
                            return ProgramLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('program-level', null, { reload: 'program-level' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
